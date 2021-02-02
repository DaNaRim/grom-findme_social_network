package com.findme.service;

import com.findme.dao.UserDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userdao;

    @Autowired
    public UserServiceImpl(UserDao userdao) {
        this.userdao = userdao;
    }

    @Override
    public User findById(long id) throws NotFoundException, InternalServerException {
        User user = userdao.findById(id);

        if (user == null) {
            throw new NotFoundException("Missing user with id " + id);
        }

        return user;
    }

    @Override
    public boolean isUserMissing(long id) throws InternalServerException {
        return userdao.isUserMissing(id);
    }

    @Override
    public boolean isUsersMissing(List<User> users) throws InternalServerException {
        return userdao.isUsersMissing(users);
    }

    @Override
    public User registerUser(User user) throws BadRequestException, InternalServerException {
        validateCreateUser(user);

        return userdao.save(user);
    }

    @Override
    public User updateUser(User user) throws BadRequestException, InternalServerException {
        validateUpdateUser(user);

        return userdao.update(user);
    }

    @Override
    public User login(String mail, String password) throws BadRequestException, InternalServerException {

        User user = userdao.findByMail(mail);

        if (user == null || !user.getPassword().equals(password)) {
            throw new BadRequestException("Wrong mail or password");
        }

        return user;
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        userdao.updateDateLastActive(userId);
    }

    private void validateUserFields(User user) throws BadRequestException {

        if (user.getFirstName() == null || user.getLastName() == null
                || user.getPhone() == null || user.getMail() == null
                || user.getPassword() == null) {

            throw new BadRequestException("firstName, lastName, phone, mail and password are required fields");

        } else if (user.getFirstName().length() > 30
                || user.getLastName().length() > 30
                || user.getPhone().length() > 15
                || user.getCountry() != null && user.getCountry().length() > 30
                || user.getCity() != null && user.getCity().length() > 30
                || user.getReligion() != null && user.getReligion().length() > 30
                || user.getSchool() != null && user.getSchool().length() > 30
                || user.getUniversity() != null && user.getUniversity().length() > 30) {

            throw new BadRequestException("fields length too long");

        } else if (user.getPassword().length() < 8) {
            throw new BadRequestException("password length must be greater than 8");

        } else if (user.getAge() != null && user.getAge() <= 0) {
            throw new BadRequestException("age filed incorrect");
        }
    }

    private void validateCreateUser(User user) throws BadRequestException, InternalServerException {

        validateUserFields(user);

        if (userdao.arePhoneAndMailBusy(user.getPhone(), user.getMail())) {
            throw new BadRequestException("mail or phone is busy");
        }
    }

    private void validateUpdateUser(User user) throws BadRequestException, InternalServerException {

        validateUserFields(user);

        String oldPhone = userdao.findPhone(user.getId());
        String oldMail = userdao.findMail(user.getId());

        if (!user.getPhone().equals(oldPhone) && userdao.isPhoneBusy(user.getPhone())) {
            throw new BadRequestException("phone is busy");

        } else if (!user.getMail().equals(oldMail) && userdao.isMailBusy(user.getMail())) {
            throw new BadRequestException("mail is busy");
        }
    }
}
