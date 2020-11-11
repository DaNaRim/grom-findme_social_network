package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    public User save(User user) throws InternalServerException {
        try {
            em.persist(user);

            return user;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to save user" + e.getMessage());
        }
    }

    public User findById(long id) throws ObjectNotFoundException, InternalServerException {
        try {
            User user = em.find(User.class, id);

            if (user == null) {
                throw new ObjectNotFoundException("Missing user with id " + id);
            }

            return user;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to find user by id " + id + " : "
                    + e.getMessage());
        }
    }

    public User update(User user) throws InternalServerException {
        try {
            return em.merge(user);

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to update user " + user.getId() + " : "
                    + e.getMessage());
        }
    }

    public void delete(User user) throws InternalServerException {
        try {
            em.remove(em.merge(user));

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to delete user " + user.getId() + " : "
                    + e.getMessage());
        }
    }
}
