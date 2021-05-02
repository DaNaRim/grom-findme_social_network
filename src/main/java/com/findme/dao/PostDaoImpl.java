package com.findme.dao;

import com.findme.model.Post;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDaoImpl extends Dao<Post> implements PostDao {

    private static final String QUERY_FIND_BY_USER_PAGE_POSTED =
            "SELECT p.* , array_agg(u.id) AS tagged_users"
                    + " FROM Post p"
                    + "     JOIN Post_tagged_users ptu ON p.id = ptu.post_id"
                    + "     JOIN Users u ON u.id = ptu.tagged_user_id"
                    + " WHERE user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " GROUP BY p.id"
                    + " ORDER BY max(p.date_posted)"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT " + PostDaoImpl.ATTRIBUTE_RESULT_LIMIT;

    private static final String QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED =
            "SELECT p.* , array_agg(u.id) AS tagged_users"
                    + " FROM Post p"
                    + "     JOIN Post_tagged_users ptu ON p.id = ptu.post_id"
                    + "     JOIN Users u ON u.id = ptu.tagged_user_id"
                    + " WHERE user_posted = :" + PostDaoImpl.ATTRIBUTE_USER_POSTED_ID
                    + "   AND user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " GROUP BY p.id"
                    + " ORDER BY max(p.date_posted)"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT " + PostDaoImpl.ATTRIBUTE_RESULT_LIMIT;

    private static final String QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS =
            "SELECT p.* , array_agg(u.id) AS tagged_users"
                    + " FROM Post p"
                    + "     JOIN Post_tagged_users ptu ON p.id = ptu.post_id"
                    + "     JOIN Users u ON u.id = ptu.tagged_user_id"
                    + " WHERE user_page_posted = :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + "   AND user_posted != :" + PostDaoImpl.ATTRIBUTE_USER_PAGE_POSTED_ID
                    + " GROUP BY p.id"
                    + " ORDER BY max(p.date_posted)"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT " + PostDaoImpl.ATTRIBUTE_RESULT_LIMIT;

    private static final String QUERY_GET_FEEDS_BY_USER =
            "SELECT p.* , array_agg(u.id) AS tagged_users"
                    + " FROM Post p"
                    + "     JOIN Post_tagged_users ptu ON p.id = ptu.post_id"
                    + "     JOIN Users u ON u.id = ptu.tagged_user_id"
                    + "     JOIN Relationship r"
                    + "          ON r.user_from = :" + PostDaoImpl.ATTRIBUTE_USER_ID
                    + "              AND r.user_to = p.user_posted"
                    + "            OR r.user_from = p.user_posted"
                    + "              AND r.user_to = :" + PostDaoImpl.ATTRIBUTE_USER_ID
                    + " WHERE r.status = 'FRIENDS'"
                    + " GROUP BY p.id"
                    + " ORDER BY max(p.date_posted)"
                    + " OFFSET :" + PostDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT " + PostDaoImpl.ATTRIBUTE_RESULT_LIMIT;


    private static final String QUERY_IS_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID + ")";

    private static final String QUERY_FIND_USER_POSTED_BY_ID =
            "SELECT user_posted FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_USER_PAGE_POSTED_BY_ID =
            "SELECT user_page_posted FROM Post WHERE id = :" + PostDaoImpl.ATTRIBUTE_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_USER_PAGE_POSTED_ID = "userPagePosted";
    private static final String ATTRIBUTE_USER_POSTED_ID = "userPosted";
    private static final String ATTRIBUTE_USER_ID = "userId"; //for feeds (Relationship: userFrom and userTo)

    private static final String ATTRIBUTE_START_FROM = "startFrom";
    private static final int ATTRIBUTE_RESULT_LIMIT = 10;

    public PostDaoImpl() {
        super(Post.class);
    }

    @Override
    public Post update(Post post) {

        post.setDateUpdated(new Date());
        return super.update(post);
    }

    @Override
    public List<Post> findByUserPagePosted(long userId, long startFrom) {

        List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_PAGE_POSTED, Post.class)
                .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
                .setParameter(ATTRIBUTE_START_FROM, startFrom)
                .getResultList();

        return posts == null ? new ArrayList<>() : posts;
    }

    @Override
    public List<Post> findByUserPostedAndUserPagePosted(long userPostedId, long userPagePostedId, long startFrom) {

        List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED, Post.class)
                .setParameter(ATTRIBUTE_USER_POSTED_ID, userPostedId)
                .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userPagePostedId)
                .setParameter(ATTRIBUTE_START_FROM, startFrom)
                .getResultList();

        return posts == null ? new ArrayList<>() : posts;
    }

    @Override
    public List<Post> findByUserPagePostedOnlyFriends(long userId, long startFrom) {

        List<Post> posts = em.createNativeQuery(QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS, Post.class)
                .setParameter(ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
                .setParameter(ATTRIBUTE_START_FROM, startFrom)
                .getResultList();

        return posts == null ? new ArrayList<>() : posts;
    }

    @Override
    public List<Post> getFeedsByUser(long userId, long startFrom) {

        List<Post> posts = em.createNativeQuery(QUERY_GET_FEEDS_BY_USER, Post.class)
                .setParameter(ATTRIBUTE_USER_ID, userId)
                .setParameter(ATTRIBUTE_START_FROM, startFrom)
                .getResultList();

        return posts == null ? new ArrayList<>() : posts;
    }


    @Override
    public boolean isPostMissing(long id) {

        return !(boolean) em.createNativeQuery(QUERY_IS_EXISTS)
                .setParameter(ATTRIBUTE_ID, id)
                .getSingleResult();
    }

    @Override
    public Long findUserPostedId(long postId) {
        try {
            Integer id = (Integer) em.createNativeQuery(QUERY_FIND_USER_POSTED_BY_ID)
                    .setParameter(ATTRIBUTE_ID, postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Long findUserPagePostedId(long postId) {
        try {
            Integer id = (Integer) em.createNativeQuery(QUERY_FIND_USER_PAGE_POSTED_BY_ID)
                    .setParameter(ATTRIBUTE_ID, postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        }
    }
}
