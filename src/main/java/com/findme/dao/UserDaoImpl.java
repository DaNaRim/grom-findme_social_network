package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class UserDaoImpl extends Dao<User> implements UserDao {

    private static final String QUERY_FIND_BY_MAIL =
            "SELECT * FROM Users WHERE mail = :" + UserDaoImpl.ATTRIBUTE_MAIL;

    private static final String QUERY_IS_USER_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID + ")";

    private static final String QUERY_IS_USERS_EXISTS =
            "SELECT COUNT(*) FROM Users WHERE id IN :" + UserDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_UPDATE_DATE_LAST_ACTIVE =
            "UPDATE Users SET date_last_active = :" + UserDaoImpl.ATTRIBUTE_DATE_LAST_ACTIVE +
                    " WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;


    private static final String QUERY_ARE_PHONE_AND_MAIL_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE
                    + " OR mail = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")";

    private static final String QUERY_IS_PHONE_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE + ")";

    private static final String QUERY_IS_MAIL_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE mail = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")";

    private static final String QUERY_FIND_PHONE =
            "SELECT phone FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_MAIL =
            "SELECT mail FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_LIST_ID = "listId";
    private static final String ATTRIBUTE_PHONE = "phone";
    private static final String ATTRIBUTE_MAIL = "mail";
    private static final String ATTRIBUTE_DATE_LAST_ACTIVE = "dateLastActive";

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public User update(User user) throws InternalServerException {
        user.setDateLastActive(new Date());
        return super.update(user);
    }

    @Override
    public User findByMail(String mail) throws InternalServerException {
        try {
            return (User) em.createNativeQuery(QUERY_FIND_BY_MAIL, User.class)
                    .setParameter(ATTRIBUTE_MAIL, mail)
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
            return !(boolean) em.createNativeQuery(QUERY_IS_USER_EXISTS)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isUserMissing failed", e);
        }
    }


    @Override
    public boolean areUsersMissing(List<Long> usersIds) throws InternalServerException {

        if (usersIds.isEmpty()) return true;

        try {
            BigInteger countExistsUsers = (BigInteger) em.createNativeQuery(QUERY_IS_USERS_EXISTS)
                    .setParameter(ATTRIBUTE_LIST_ID, usersIds)
                    .getSingleResult();

            return countExistsUsers.intValue() != usersIds.size();
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.areUsersMissing failed", e);
        }
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        try {
            em.createNativeQuery(QUERY_UPDATE_DATE_LAST_ACTIVE)
                    .setParameter(ATTRIBUTE_DATE_LAST_ACTIVE, new Date())
                    .setParameter(ATTRIBUTE_ID, userId)
                    .executeUpdate();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.updateDateLastActive failed", e);
        }
    }

    @Override
    public boolean arePhoneAndMailBusy(String phone, String mail) throws InternalServerException {
        try {
            return (boolean) em.createNativeQuery(QUERY_ARE_PHONE_AND_MAIL_BUSY)
                    .setParameter(ATTRIBUTE_PHONE, phone)
                    .setParameter(ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.arePhoneAndMailBusy failed", e);
        }
    }


    @Override
    public boolean isPhoneBusy(String phone) throws InternalServerException {
        try {
            return (boolean) em.createNativeQuery(QUERY_IS_PHONE_BUSY)
                    .setParameter(ATTRIBUTE_PHONE, phone)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isPhoneBusy failed", e);
        }
    }

    @Override
    public boolean isMailBusy(String mail) throws InternalServerException {
        try {
            return (boolean) em.createNativeQuery(QUERY_IS_MAIL_BUSY)
                    .setParameter(ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isMailBusy failed", e);
        }
    }

    @Override
    public String findPhone(long id) throws InternalServerException {
        try {
            return (String) em.createNativeQuery(QUERY_FIND_PHONE)
                    .setParameter(ATTRIBUTE_ID, id)
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
            return (String) em.createNativeQuery(QUERY_FIND_MAIL)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findMail failed", e);
        }
    }
}
