package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class UserDaoImpl extends Dao<User> implements UserDao {

    private static final String QUERY_FIND_BY_USERNAME =
            "SELECT u.*, array_agg(r.role_name) AS user_role"
                    + " FROM Users u"
                    + "     JOIN User_role ur ON (u.id = ur.user_id)"
                    + "     JOIN Role r ON (ur.role_id = r.id)"
                    + " WHERE u.username = :" + UserDaoImpl.ATTRIBUTE_USERNAME
                    + " GROUP BY u.id";

    private static final String QUERY_IS_USER_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID + ")";

    private static final String QUERY_IS_USERS_EXISTS =
            "SELECT COUNT(*) FROM Users WHERE id IN :" + UserDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_UPDATE_DATE_LAST_ACTIVE =
            "UPDATE Users SET date_last_active = :" + UserDaoImpl.ATTRIBUTE_DATE_LAST_ACTIVE +
                    " WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;


    private static final String QUERY_ARE_PHONE_AND_MAIL_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE
                    + " OR email = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")";

    private static final String QUERY_IS_PHONE_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE + ")";

    private static final String QUERY_IS_MAIL_BUSY =
            "SELECT EXISTS(SELECT 1 FROM Users WHERE email = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")";

    private static final String QUERY_FIND_PHONE =
            "SELECT phone FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_MAIL =
            "SELECT email FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_LIST_ID = "listId";
    private static final String ATTRIBUTE_USERNAME = "username"; //system field
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
    public User findByUsername(String username) {
        try {
            return (User) em.createNativeQuery(QUERY_FIND_BY_USERNAME, User.class)
                    .setParameter(ATTRIBUTE_USERNAME, username)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean isUserMissing(long id) {

        return !(boolean) em.createNativeQuery(QUERY_IS_USER_EXISTS)
                .setParameter(ATTRIBUTE_ID, id)
                .getSingleResult();
    }


    @Override
    public boolean areUsersMissing(List<Long> usersIds) {

        if (usersIds.isEmpty()) return true;

        BigInteger countExistsUsers = (BigInteger) em.createNativeQuery(QUERY_IS_USERS_EXISTS)
                .setParameter(ATTRIBUTE_LIST_ID, usersIds)
                .getSingleResult();

        return countExistsUsers.intValue() != usersIds.size();
    }

    @Override
    public void updateDateLastActive(long userId) {

        em.createNativeQuery(QUERY_UPDATE_DATE_LAST_ACTIVE)
                .setParameter(ATTRIBUTE_DATE_LAST_ACTIVE, new Date())
                .setParameter(ATTRIBUTE_ID, userId)
                .executeUpdate();
    }

    @Override
    public boolean arePhoneAndMailBusy(String phone, String mail) {

        return (boolean) em.createNativeQuery(QUERY_ARE_PHONE_AND_MAIL_BUSY)
                .setParameter(ATTRIBUTE_PHONE, phone)
                .setParameter(ATTRIBUTE_MAIL, mail)
                .getSingleResult();
    }


    @Override
    public boolean isPhoneBusy(String phone) {

        return (boolean) em.createNativeQuery(QUERY_IS_PHONE_BUSY)
                .setParameter(ATTRIBUTE_PHONE, phone)
                .getSingleResult();
    }

    @Override
    public boolean isMailBusy(String mail) {

        return (boolean) em.createNativeQuery(QUERY_IS_MAIL_BUSY)
                .setParameter(ATTRIBUTE_MAIL, mail)
                .getSingleResult();
    }

    @Override
    public String findPhone(long id) {
        try {
            return (String) em.createNativeQuery(QUERY_FIND_PHONE)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public String findMail(long id) {
        try {
            return (String) em.createNativeQuery(QUERY_FIND_MAIL)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }
}
