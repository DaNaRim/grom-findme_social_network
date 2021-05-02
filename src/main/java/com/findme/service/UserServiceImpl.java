package com.findme.service;

import com.findme.dao.RoleDao;
import com.findme.dao.UserDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.RoleName;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional
public class UserServiceImpl implements UserService {

    private final UserDao userdao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userdao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userdao = userdao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userdao.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not exists by this username");
        }
        return user;
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
    public boolean isUsersMissing(List<Long> usersIds) throws InternalServerException {
        return userdao.areUsersMissing(usersIds);
    }

    @Override
    public User registerUser(User user) throws BadRequestException, InternalServerException {
        validateCreateUser(user);

        user.setRoles(Collections.singleton(roleDao.findByUserRole(RoleName.USER)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userdao.save(user);
    }

    @Override
    public User updateUser(User user) throws BadRequestException, InternalServerException {
        validateUpdateUser(user);

        return userdao.update(user);
    }

    @Override
    public void updateDateLastActive(long userId) throws InternalServerException {
        userdao.updateDateLastActive(userId);
    }

    private void validateUserFields(User user) throws BadRequestException {

        if (user.getFirstName() == null || user.getLastName() == null
                || user.getUsername() == null || user.getPassword() == null
                || user.getPhone() == null || user.getEmail() == null) {

            throw new BadRequestException("firstName, lastName, username, password, phone and email " +
                    "are required fields");

        } else if (user.getFirstName().length() > 30
                || user.getLastName().length() > 30
                || user.getUsername().length() > 30
                || user.getPhone().length() > 15
                || user.getCountry() != null && user.getCountry().length() > 30
                || user.getCity() != null && user.getCity().length() > 30
                || user.getSchool() != null && user.getSchool().length() > 30
                || user.getUniversity() != null && user.getUniversity().length() > 30) {

            throw new BadRequestException("fields length must be less than 30 characters");

        } else if (user.getPassword().length() < 8) {
            throw new BadRequestException("password length must have at least 8 character");

        } else if (user.getDateOfBirth() != null && user.getDateOfBirth().after(new Date())) {
            throw new BadRequestException("dateOfBirth filed incorrect");
        }
    }

    private void validateCreateUser(User user) throws BadRequestException, InternalServerException {

        validateUserFields(user);

        if (userdao.arePhoneAndMailBusy(user.getPhone(), user.getEmail())) { //TODO rework
            throw new BadRequestException("mail or phone is busy");
        }
    }

    private void validateUpdateUser(User user) throws BadRequestException, InternalServerException {

        validateUserFields(user);

        String oldPhone = userdao.findPhone(user.getId());
        String oldMail = userdao.findMail(user.getId());

        if (!user.getPhone().equals(oldPhone) && userdao.isPhoneBusy(user.getPhone())) {
            throw new BadRequestException("phone is busy");

        } else if (!user.getEmail().equals(oldMail) && userdao.isMailBusy(user.getEmail())) {
            throw new BadRequestException("mail is busy");
        }
    }
}
