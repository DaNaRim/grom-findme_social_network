package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.Date;

public class UserDaoImpl extends Dao<User> implements UserDao {

    private static final String IS_EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE ID = :id)";
    private static final String FIND_BY_MAIL_QUERY = "SELECT * FROM USERS WHERE MAIL = :mail";

    private static final String ARE_THE_PHONE_AND_MAIL_BUSY_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE PHONE = :phone OR MAIL = :mail)";

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User save(User entity) throws InternalServerException {
        entity.setDateLastActive(new Date());

        return super.save(entity);
    }

    @Override
    public boolean isUserMissing(long id) throws InternalServerException {
        try {
            return !(boolean) em.createNativeQuery(IS_EXISTS_QUERY)
                    .setParameter("id", id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isUserMissing failed: " + e.getMessage());
        }
    }

    @Override
    public User findByMail(String mail) throws InternalServerException {
        try {
            return (User) em.createNativeQuery(FIND_BY_MAIL_QUERY, User.class)
                    .setParameter("mail", mail)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findByMail failed: " + e.getMessage());
        }
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        try {
            User user = findById(userId);
            user.setDateLastActive(new Date());

            em.merge(user);
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.updateDateLastActive failed: " + e.getMessage());
        }
    }


    @Override
    public boolean areThePhoneAndMailBusy(String phone, String mail) throws InternalServerException {
        try {
            return (boolean) em.createNativeQuery(ARE_THE_PHONE_AND_MAIL_BUSY_QUERY)
                    .setParameter("phone", phone)
                    .setParameter("mail", mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.areThePhoneAndMailBusy failed: " + e.getMessage());
        }
    }
}
