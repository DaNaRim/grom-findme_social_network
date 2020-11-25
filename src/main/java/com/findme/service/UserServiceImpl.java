package com.findme.service;

import com.findme.dao.UserDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.ObjectNotFoundException;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    private final UserDao userdao;

    @Autowired
    public UserServiceImpl(UserDao userdao) {
        this.userdao = userdao;
    }

    public User findById(long id) throws ObjectNotFoundException, InternalServerException {
        return userdao.findById(id);
    }

    public User registerUser(User user) throws BadRequestException, InternalServerException {
        try {
            validateUser(user);

            return userdao.save(user);
        } catch (BadRequestException e) {
            throw new BadRequestException("Can`t register user: " + e.getMessage());
        }
    }


    private void validateUser(User user) throws BadRequestException, InternalServerException {

        if (user.getFirstName() == null || user.getLastName() == null
                || user.getPhone() == null || user.getMail() == null
                || user.getPassWord() == null) {
            throw new BadRequestException("firstName, lastName, phone, mail and passWord are required fields");
        }
        if (user.getFirstName().length() > 30
                || user.getLastName().length() > 30
                || user.getPhone().length() > 30
                || user.getMail().length() > 30
                || user.getPassWord().length() > 30
                || user.getCountry() != null && user.getCountry().length() > 30
                || user.getCity() != null && user.getCity().length() > 30
                || user.getRelationshipStatus() != null && user.getRelationshipStatus().length() > 30
                || user.getReligion() != null && user.getReligion().length() > 30
                || user.getSchool() != null && user.getSchool().length() > 30
                || user.getUniversity() != null && user.getUniversity().length() > 30
        ) {
            throw new BadRequestException("fields length must be <= 30");
        }
        if (user.getPassWord().length() < 8) {
            throw new BadRequestException("passWord length must be > 8");
        }
        if (user.getAge() != null && (user.getAge() <= 0 || user.getAge() > 150)) {
            throw new BadRequestException("age filed incorrect");
        }

        userdao.checkPhoneAndMailForUnique(user.getPhone(), user.getMail());
    }
}
