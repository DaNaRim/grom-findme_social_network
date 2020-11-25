package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.User;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
public class UserDaoImpl extends Dao<User> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    private static final String CHECK_PHONE_AND_MAIL_FOR_UNIQUE_QUERY = "SELECT EXISTS(SELECT 1 FROM USERS WHERE PHONE = :phone OR MAIL = :mail)";

    @Override
    public User save(User entity) throws InternalServerException {
        entity.setDateLastActive(new Date());

        return super.save(entity);
    }

    public void checkPhoneAndMailForUnique(String phone, String mail)
            throws BadRequestException, InternalServerException {
        try {
            boolean isExist = (Boolean) em.createNativeQuery(CHECK_PHONE_AND_MAIL_FOR_UNIQUE_QUERY)
                    .setParameter("phone", phone)
                    .setParameter("mail", mail)
                    .getSingleResult();

            if (isExist) {
                throw new BadRequestException("mail or phone is busy");
            }
        } catch (HibernateException e) {
            throw new InternalServerException("Something went wrong while trying to check phone " + phone
                    + " and mail " + mail + " for unique: " + e.getMessage());
        }
    }
}
