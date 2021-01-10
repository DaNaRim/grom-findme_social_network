package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = {HibernateException.class, InternalServerException.class})
public class PostDaoImpl extends Dao<Post> implements PostDao {

    private static final String FIND_BY_USER_PAGE_POSTED_QUERY = "SELECT * FROM POST WHERE USER_PAGE_POSTED = :userId ORDER BY DATE_POSTED";
    private static final String FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED_QUERY = "SELECT * FROM POST WHERE USER_POSTED = :userPostedId AND USER_PAGE_POSTED = :userPagePostedId ORDER BY DATE_POSTED";
    private static final String FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS_QUERY = "SELECT * FROM POST WHERE USER_PAGE_POSTED = :userId AND USER_POSTED != :userId ORDER BY DATE_POSTED";
    private static final String FIND_DATE_POSTED_QUERY = "SELECT DATE_POSTED FROM POST WHERE ID = :postId";
    private static final String FIND_USER_POSTED_QUERY = "SELECT USER_POSTED FROM POST WHERE ID = :postId";
    private static final String FIND_USER_PAGE_POSTED_QUERY = "SELECT USER_PAGE_POSTED FROM POST WHERE ID = :postId";

    public PostDaoImpl() {
        super(Post.class);
    }

    @Override
    public Post update(Post post) throws InternalServerException {

        post.setDateUpdated(new Date());

        return super.update(post);
    }

    @Override
    public List<Post> findByUserPagePosted(long userId) throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(FIND_BY_USER_PAGE_POSTED_QUERY)
                    .setParameter("userId", userId)
                    .getResultList();

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPagePosted failed: " + e.getMessage());
        }
    }

    @Override
    public List<Post> findByUserPostedAndUserPagePosted(long userPostedId, long userPagePostedId)
            throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED_QUERY)
                    .setParameter("userPostedId", userPostedId)
                    .setParameter("userPagePostedId", userPagePostedId)
                    .getResultList();

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPostedAndUserPagePosted failed: " + e.getMessage());
        }
    }

    @Override
    public List<Post> findByUserPagePostedOnlyFriends(long userId) throws InternalServerException {
        try {
            List<Post> posts = em.createNativeQuery(FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS_QUERY)
                    .setParameter("userId", userId)
                    .getResultList();

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPagePostedOnlyFriends failed: " + e.getMessage());
        }
    }

    @Override
    public Date findDatePosted(long postId) throws InternalServerException {
        try {
            return (Date) em.createNativeQuery(FIND_DATE_POSTED_QUERY)
                    .setParameter("postId", postId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findDatePosted failed: " + e.getMessage());
        }
    }

    @Override
    public Long findUserPostedId(long postId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(FIND_USER_POSTED_QUERY)
                    .setParameter("postId", postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findUserPostedId failed: " + e.getMessage());
        }
    }

    @Override
    public Long findUserPagePostedId(long postId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(FIND_USER_PAGE_POSTED_QUERY)
                    .setParameter("postId", postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findUserPagePostedId failed: " + e.getMessage());
        }
    }
}
