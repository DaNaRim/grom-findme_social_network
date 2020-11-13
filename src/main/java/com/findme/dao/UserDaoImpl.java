package com.findme.dao;

import com.findme.exception.BadRequestException;
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

    private static final String CHECK_PHONE_FOR_UNIQUE_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE PHONE = :phone)";
    private static final String CHECK_MAIL_FOR_UNIQUE_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE MAIL = :mail)";

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

    public void checkPhoneForUnique(String phone) throws BadRequestException, InternalServerException {
        try {
            boolean check = (Boolean) em.createNativeQuery(CHECK_PHONE_FOR_UNIQUE_QUERY)
                    .setParameter("phone", phone)
                    .getSingleResult();

            if (!check) {
                throw new BadRequestException("user with this phone already exists");
            }
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check phone " + phone
                    + " for unique: " + e.getMessage());
        }
    }

    public void checkMailForUnique(String mail) throws BadRequestException, InternalServerException {
        try {
            boolean check = (Boolean) em.createNativeQuery(CHECK_MAIL_FOR_UNIQUE_QUERY)
                    .setParameter("mail", mail)
                    .getSingleResult();

            if (!check) {
                throw new BadRequestException("user with this mail already exists");
            }
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check mail " + mail
                    + " for unique: " + e.getMessage());
        }
    }
}
