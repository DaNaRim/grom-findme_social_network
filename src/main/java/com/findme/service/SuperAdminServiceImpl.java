package com.findme.service;

import com.findme.dao.RoleDao;
import com.findme.dao.UserDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.User;
import com.findme.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public SuperAdminServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public void makeAdmin(long userId) throws BadRequestException, InternalServerException {

        User user = userDao.findById(userId);

        if (user == null) {
            throw new BadRequestException("Missing user with this id");

        } else if (roleDao.isUserAdmin(userId)) {
            throw new BadRequestException("User is already admin");
        }

        Set<Role> roles = user.getRoles();
        roles.add(new Role(UserRole.ADMIN));
        user.setRoles(roles);

        userDao.update(user);
    }

    @Override
    public void removeAdmin(long userId) throws BadRequestException, InternalServerException {

        User user = userDao.findById(userId);

        if (user == null) {
            throw new BadRequestException("Missing user with this id");

        } else if (!roleDao.isUserAdmin(userId)) {
            throw new BadRequestException("User is not admin");
        }

        Set<Role> roles = user.getRoles();
        roles.removeIf(role -> role.getUserRole().equals(UserRole.ADMIN));
        user.setRoles(roles);

        userDao.update(user);
    }
}
