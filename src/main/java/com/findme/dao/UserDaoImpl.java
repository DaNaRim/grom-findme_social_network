package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;

import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

@MappedSuperclass
@NamedNativeQueries({
        @NamedNativeQuery(name = UserDaoImpl.QUERY_FIND_BY_MAIL,
                query = "SELECT * FROM Users WHERE mail = :" + UserDaoImpl.ATTRIBUTE_MAIL,
                resultClass = User.class),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_IS_EXISTS,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID + ")"),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_UPDATE_DATE_LAST_ACTIVE,
                query = "UPDATE Users SET date_last_active = :" + UserDaoImpl.ATTRIBUTE_DATE_LAST_ACTIVE +
                        " WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID),


        @NamedNativeQuery(name = UserDaoImpl.QUERY_ARE_PHONE_AND_MAIL_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE
                        + " OR mail = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")"),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_IS_PHONE_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE phone = :" + UserDaoImpl.ATTRIBUTE_PHONE + ")"),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_IS_MAIL_BUSY,
                query = "SELECT EXISTS(SELECT 1 FROM Users WHERE mail = :" + UserDaoImpl.ATTRIBUTE_MAIL + ")"),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_FIND_PHONE,
                query = "SELECT phone FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID),

        @NamedNativeQuery(name = UserDaoImpl.QUERY_FIND_MAIL,
                query = "SELECT mail FROM Users WHERE id = :" + UserDaoImpl.ATTRIBUTE_ID),
})
public class UserDaoImpl extends Dao<User> implements UserDao {

    public static final String QUERY_FIND_BY_MAIL = "findByMail";
    public static final String QUERY_IS_EXISTS = "isExists";
    public static final String QUERY_UPDATE_DATE_LAST_ACTIVE = "updateDateLastActive";

    public static final String QUERY_ARE_PHONE_AND_MAIL_BUSY = "arePhoneAndMailBusy";
    public static final String QUERY_IS_PHONE_BUSY = "isPhoneBusy";
    public static final String QUERY_IS_MAIL_BUSY = "isMailBusy";
    public static final String QUERY_FIND_PHONE = "findPhone";
    public static final String QUERY_FIND_MAIL = "findMail";

    public static final String ATTRIBUTE_ID = "id";
    public static final String ATTRIBUTE_PHONE = "phone";
    public static final String ATTRIBUTE_MAIL = "mail";
    public static final String ATTRIBUTE_DATE_LAST_ACTIVE = "dateLastActive";

    public UserDaoImpl() {
        super(User.class);
    }

    private static String getIsUsersMissingQuery(List<User> users) {

        StringBuilder query = new StringBuilder();
        for (User user : users) {
            query.append("SELECT EXISTS(SELECT 1 FROM Users WHERE id = ").append(user.getId()).append(")");
            query.append(" INTERSECT ");
        }
        query.delete(query.lastIndexOf(" INTERSECT "), query.length());

        return query.toString();
    }

    @Override
    public User findByMail(String mail) throws InternalServerException {
        try {
            return em.createNamedQuery(QUERY_FIND_BY_MAIL, User.class)
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
            return !(boolean) em.createNamedQuery(QUERY_IS_EXISTS)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isUserMissing failed", e);
        }
    }

    @Override
    public User update(User user) throws InternalServerException {
        user.setDateLastActive(new Date());
        return super.update(user);
    }

    @Override
    public boolean isUsersMissing(List<User> users) throws InternalServerException {

        if (users.isEmpty()) return false;

        try {
            return !(boolean) em.createNativeQuery(getIsUsersMissingQuery(users))
                    .getSingleResult();

        } catch (NoResultException e) {
            return true;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isUsersMissing failed", e);
        }
    }

    @Override
    public boolean arePhoneAndMailBusy(String phone, String mail) throws InternalServerException {
        try {
            return (boolean) em.createNamedQuery(QUERY_ARE_PHONE_AND_MAIL_BUSY)
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
            return (boolean) em.createNamedQuery(QUERY_IS_PHONE_BUSY)
                    .setParameter(ATTRIBUTE_PHONE, phone)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isPhoneBusy failed", e);
        }
    }

    @Override
    public boolean isMailBusy(String mail) throws InternalServerException {
        try {
            return (boolean) em.createNamedQuery(QUERY_IS_MAIL_BUSY)
                    .setParameter(ATTRIBUTE_MAIL, mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.isMailBusy failed", e);
        }
    }

    @Override
    public String findPhone(long id) throws InternalServerException {
        try {
            return (String) em.createNamedQuery(QUERY_FIND_PHONE)
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
            return (String) em.createNamedQuery(QUERY_FIND_MAIL)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.findMail failed", e);
        }
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        try {
            em.createNamedQuery(QUERY_UPDATE_DATE_LAST_ACTIVE)
                    .setParameter(ATTRIBUTE_DATE_LAST_ACTIVE, new Date())
                    .setParameter(ATTRIBUTE_ID, userId)
                    .executeUpdate();

        } catch (HibernateException e) {
            throw new InternalServerException("UserDaoImpl.updateDateLastActive failed", e);
        }
    }
}
