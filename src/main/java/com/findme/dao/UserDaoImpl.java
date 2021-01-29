package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.Date;

public class UserDaoImpl extends Dao<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User findByMail(String mail) throws InternalServerException {
        try {
            return em.createNamedQuery(User.QUERY_FIND_BY_MAIL, User.class)
                    .setParameter(User.ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findByMail failed", e);
        }
    }

    @Override
    public boolean isUserMissing(long id) throws InternalServerException {
        try {
            return !(boolean) em.createNamedQuery(User.QUERY_IS_EXISTS)
                    .setParameter(User.ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isUserMissing failed", e);
        }
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        try {
            User user = findById(userId);
            user.setDateLastActive(new Date());

            em.merge(user);
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.updateDateLastActive failed", e);
        }
    }


    @Override
    public boolean arePhoneAndMailBusy(String phone, String mail) throws InternalServerException {
        try {
            return (boolean) em.createNamedQuery(User.QUERY_ARE_PHONE_AND_MAIL_BUSY)
                    .setParameter(User.ATTRIBUTE_PHONE, phone)
                    .setParameter(User.ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.arePhoneAndMailBusy failed", e);
        }
    }

    @Override
    public boolean isPhoneBusy(String phone) throws InternalServerException {
        try {
            return (boolean) em.createNamedQuery(User.QUERY_IS_PHONE_BUSY)
                    .setParameter(User.ATTRIBUTE_PHONE, phone)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isPhoneBusy failed", e);
        }
    }

    @Override
    public boolean isMailBusy(String mail) throws InternalServerException {
        try {
            return (boolean) em.createNamedQuery(User.QUERY_IS_MAIL_BUSY)
                    .setParameter(User.ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isMailBusy failed", e);
        }
    }

    @Override
    public String findPhone(long id) throws InternalServerException {
        try {
            return (String) em.createNamedQuery(User.QUERY_FIND_PHONE)
                    .setParameter(User.ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findPhone failed", e);
        }
    }

    @Override
    public String findMail(long id) throws InternalServerException {
        try {
            return (String) em.createNamedQuery(User.QUERY_FIND_MAIL)
                    .setParameter(User.ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findMail failed", e);
        }
    }
}
