package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Post;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostDaoImpl extends Dao<Post> implements PostDao {

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
            List<Post> posts = em.createNamedQuery(Post.QUERY_FIND_BY_USER_PAGE_POSTED)
                    .setParameter(Post.ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
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
            List<Post> posts = em.createNamedQuery(Post.QUERY_FIND_BY_USER_POSTED_AND_USER_PAGE_POSTED)
                    .setParameter(Post.ATTRIBUTE_USER_POSTED_ID, userPostedId)
                    .setParameter(Post.ATTRIBUTE_USER_PAGE_POSTED_ID, userPagePostedId)
                    .getResultList();

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPostedAndUserPagePosted failed: " + e.getMessage());
        }
    }

    @Override
    public List<Post> findByUserPagePostedOnlyFriends(long userId) throws InternalServerException {
        try {
            List<Post> posts = em.createNamedQuery(Post.QUERY_FIND_BY_USER_PAGE_POSTED_ONLY_FRIENDS)
                    .setParameter(Post.ATTRIBUTE_USER_PAGE_POSTED_ID, userId)
                    .getResultList();

            return posts == null ? new ArrayList<>() : posts;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findByUserPagePostedOnlyFriends failed: " + e.getMessage());
        }
    }


    @Override
    public Long findUserPostedId(long postId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNamedQuery(Post.QUERY_FIND_USER_POSTED_BY_ID)
                    .setParameter(Post.ATTRIBUTE_ID, postId)
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
            Integer id = (Integer) em.createNamedQuery(Post.QUERY_FIND_USER_PAGE_POSTED_BY_ID)
                    .setParameter(Post.ATTRIBUTE_ID, postId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("PostDaoImpl.findUserPagePostedId failed: " + e.getMessage());
        }
    }
}
