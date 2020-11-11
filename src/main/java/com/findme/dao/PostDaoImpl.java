package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.Post;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class PostDaoImpl implements PostDao {

    @PersistenceContext
    private EntityManager em;

    public Post save(Post post) throws InternalServerException {
        try {
            em.persist(post);

            return post;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to save post" + e.getMessage());
        }
    }

    public Post findById(long id) throws ObjectNotFoundException, InternalServerException {
        try {
            Post post = em.find(Post.class, id);

            if (post == null) {
                throw new ObjectNotFoundException("Missing post with id " + id);
            }

            return post;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to find post by id " + id + " : "
                    + e.getMessage());
        }
    }

    public Post update(Post post) throws InternalServerException {
        try {
            return em.merge(post);

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to update post " + post.getId() + " : "
                    + e.getMessage());
        }
    }

    public void delete(Post post) throws InternalServerException {
        try {
            em.remove(em.merge(post));

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to delete post " + post.getId() + " : "
                    + e.getMessage());
        }
    }
}
