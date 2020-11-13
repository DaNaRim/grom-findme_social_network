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
            throw new BadRequestException("Cant register user: " + e.getMessage());
        }
    }


    private void validateUser(User user) throws BadRequestException, InternalServerException {

        if (user.getFirstName() == null || user.getLastName() == null
                || user.getPhone() == null || user.getMail() == null) {
            throw new BadRequestException("firstName, lastName, phone and mail are required fields");
        }

        if (user.getFirstName().length() > 20) {
            throw new BadRequestException("firstName length must be <= 20");
        }
        if (user.getLastName().length() > 20) {
            throw new BadRequestException("lastName length must be <= 20");
        }
        if (user.getPhone().length() > 20) {
            throw new BadRequestException("phone length must be <= 20");
        }
        if (user.getMail().length() > 30) {
            throw new BadRequestException("mail length must be <= 30");
        }
        if (user.getCountry() != null && user.getCountry().length() > 20) {
            throw new BadRequestException("country length must be <= 20");
        }
        if (user.getCity() != null && user.getCity().length() > 20) {
            throw new BadRequestException("city length must be <= 20");
        }
        if (user.getAge() <= 0 || user.getAge() > 150) {
            throw new BadRequestException("age filed incorrect");
        }
        if (user.getRelationshipStatus() != null && user.getRelationshipStatus().length() > 20) {
            throw new BadRequestException("relationshipStatus length must be <= 20");
        }
        if (user.getReligion() != null && user.getReligion().length() > 20) {
            throw new BadRequestException("religion length must be <= 20");
        }
        if (user.getSchool() != null && user.getSchool().length() > 30) {
            throw new BadRequestException("school length must be <= 30");
        }
        if (user.getUniversity() != null && user.getUniversity().length() > 30) {
            throw new BadRequestException("university length must be <= 30");
        }

        userdao.checkPhoneForUnique(user.getPhone());
        userdao.checkMailForUnique(user.getMail());
    }
}
