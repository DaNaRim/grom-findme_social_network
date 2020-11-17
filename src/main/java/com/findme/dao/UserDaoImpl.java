package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;

@Transactional
public class UserDaoImpl extends Dao<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @PersistenceContext
    private EntityManager em;

    private static final String CHECK_PHONE_FOR_UNIQUE_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE PHONE = :phone)";
    private static final String CHECK_MAIL_FOR_UNIQUE_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE MAIL = :mail)";

    @Override
    public User save(User entity) throws InternalServerException {
        entity.setDateLastActive(new Date());

        return super.save(entity);
    }

    public void checkPhoneForUnique(String phone) throws BadRequestException, InternalServerException {
        try {
            boolean isExist = (Boolean) em.createNativeQuery(CHECK_PHONE_FOR_UNIQUE_QUERY)
                    .setParameter("phone", phone)
                    .getSingleResult();

            if (isExist) {
                throw new BadRequestException("user with this phone already exists");
            }
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check phone " + phone
                    + " for unique: " + e.getMessage());
        }
    }

    public void checkMailForUnique(String mail) throws BadRequestException, InternalServerException {
        try {
            boolean isExist = (Boolean) em.createNativeQuery(CHECK_MAIL_FOR_UNIQUE_QUERY)
                    .setParameter("mail", mail)
                    .getSingleResult();

            if (isExist) {
                throw new BadRequestException("user with this mail already exists");
            }
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check mail " + mail
                    + " for unique: " + e.getMessage());
        }
    }
}
