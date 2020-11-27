package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Date;

@Transactional
public class UserDaoImpl extends Dao<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    private static final String FIND_BY_MAIL_QUERY = "SELECT * FROM USERS WHERE MAIL = :mail";
    private static final String ARE_THE_PHONE_AND_MAIL_BUSY_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE PHONE = :phone OR MAIL = :mail)";

    @Override
    public User save(User entity) throws InternalServerException {
        entity.setDateLastActive(new Date());

        return super.save(entity);
    }

    public User findByMail(String mail) throws InternalServerException {
        try {
            return (User) em.createNativeQuery(FIND_BY_MAIL_QUERY, User.class)
                    .setParameter("mail", mail)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to find user by mail: "
                    + e.getMessage());
        }
    }

    public boolean areThePhoneAndMailBusy(String phone, String mail) throws InternalServerException {
        try {
            return (Boolean) em.createNativeQuery(ARE_THE_PHONE_AND_MAIL_BUSY_QUERY)
                    .setParameter("phone", phone)
                    .setParameter("mail", mail)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check phone " + phone
                    + " and mail " + mail + " for availability: " + e.getMessage());
        }
    }
}
