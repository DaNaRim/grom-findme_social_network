package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDaoImpl extends Dao<Post> implements PostDao {

    private static final String QUERY_FIND_BY_USER_PAGE_POSTED =
            "SELECT * FROM Post"
                    + " WHERE user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " ORDER BY date_posted"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT 10";

    private static final String QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED =
            "SELECT * FROM Post"
                    + " WHERE user_posted = :" + PostDaoImpl.ATTRIBUTE_USER_POSTED_ID
                    + " AND user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " ORDER BY date_posted"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT 10";

    private static final String QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS =
            "SELECT * FROM Post"
                    + " WHERE user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " AND user_posted != :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " ORDER BY date_posted"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT 10";

    private static final String QUERY_GET_FEEDS_BY_USER =
            "SELECT * FROM Post"
                    + " WHERE user_posted IN ("
                    + "     SELECT user_from FROM Relationship"
                    + "     WHERE user_to = :" + PostDaoImpl.ATTRIBUTE_USER_ID
                    + "       AND status = 'FRIENDS'"
                    + "     UNION"
                    + "     SELECT user_to FROM Relationship"
                    + "     WHERE user_from = :" + PostDaoImpl.ATTRIBUTE_USER_ID
                    + "       AND status = 'FRIENDS')"
                    + "    OR user_posted = :" + PostDaoImpl.ATTRIBUTE_USER_ID
                    + " ORDER BY date_posted"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT 10";


    private static final String QUERY_IS_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID + ")";

    private static final String QUERY_FIND_USER_POSTED_BY_ID =
            "SELECT user_posted FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_USER_PAGE_POSTED_BY_ID =
            "SELECT user_page_posted FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_USER_PAGE_POSTED_ID = "userPagePosted";
    private static final String ATTRIBUTE_USER_POSTED_ID = "userPosted";
    private static final String ATTRIBUTE_USER_ID = "userId"; // for feeds

    private static final String ATTRIBUTE_START_FROM = "startFrom";

    public PostDaoImpl() {
        super(Post.class);
    }

    @Override
    public Post update(Post post) throws InternalServerException {

        post.setDateUpdated(new Date());

        return super.update(post);
    }

    @Override
    public List<Post> findByUserPagePosted(long userId, long startFrom) throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_PAGE_POSTED, Post.class)
                    .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
                    .setParameter(ATTRIBUTE_START_FROM, startFrom)
                    .getResultList();

            if (posts != null) {
                for (Post post : posts) {
                    Hibernate.initialize(post.getTaggedUsers());
                }
            }

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPagePosted failed", e);
        }
    }

    @Override
    public List<Post> findByUserPostedAndUserPagePosted(long userPostedId, long userPagePostedId, long startFrom)
            throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED, Post.class)
                    .setParameter(ATTRIBUTE_USER_POSTED_ID, userPostedId)
                    .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userPagePostedId)
                    .setParameter(ATTRIBUTE_START_FROM, startFrom)
                    .getResultList();

            if (posts != null) {
                for (Post post : posts) {
                    Hibernate.initialize(post.getTaggedUsers());
                }
            }

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPostedAndUserPagePosted failed", e);
        }
    }

    @Override
    public List<Post> findByUserPagePostedOnlyFriends(long userId, long startFrom) throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS, Post.class)
                    .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
                    .setParameter(ATTRIBUTE_START_FROM, startFrom)
                    .getResultList();

            if (posts != null) {
                for (Post post : posts) {
                    Hibernate.initialize(post.getTaggedUsers());
                }
            }

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPagePostedOnlyFriends failed", e);
        }
    }

    @Override
    public List<Post> getFeedsByUser(long userId, long startFrom) throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(QUERY_GET_FEEDS_BY_USER, Post.class)
                    .setParameter(ATTRIBUTE_USER_ID, userId)
                    .setParameter(ATTRIBUTE_START_FROM, startFrom)
                    .getResultList();

            if (posts != null) {
                for (Post post : posts) {
                    Hibernate.initialize(post.getTaggedUsers());
                }
            }

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.getFeedsByUser failed", e);
        }
    }


    @Override
    public boolean isPostMissing(long id) throws InternalServerException {
        try {
            return !(boolean) em.createNativeQuery(QUERY_IS_EXISTS)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.isUserMissing failed", e);
        }
    }

    @Override
    public Long findUserPostedId(long postId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(QUERY_FIND_USER_POSTED_BY_ID)
                    .setParameter(ATTRIBUTE_ID, postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findUserPostedId failed", e);
        }
    }

    @Override
    public Long findUserPagePostedId(long postId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(QUERY_FIND_USER_PAGE_POSTED_BY_ID)
                    .setParameter(ATTRIBUTE_ID, postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findUserPagePostedId failed", e);
        }
    }
}
