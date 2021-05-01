package com.findme.service;

import com.findme.dao.RoleDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;
    private final UserService userService;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao, UserService userService) {
        this.roleDao = roleDao;
        this.userService = userService;
    }

    @Override
    public void addRoleByUserId(long userId, UserRole userRole)
            throws BadRequestException, InternalServerException {

        if (userService.isUserMissing(userId)) {
            throw new BadRequestException("Missing user with this id");

        } else if (roleDao.hasUserRole(userId, userRole)) {
            throw new BadRequestException("User already has this role");
        }

        roleDao.addRoleByUserId(userId, userRole);
    }

    @Override
    public void removeRoleByUserId(long userId, UserRole userRole)
            throws BadRequestException, InternalServerException {

        if (userService.isUserMissing(userId)) {
            throw new BadRequestException("Missing user with this id");

        } else if (!roleDao.hasUserRole(userId, userRole)) {
            throw new BadRequestException("User hasn't this role");
        }

        roleDao.removeRoleByUserId(userId, userRole);
    }

    @Override
    public Role findByUserRole(UserRole userRole) throws InternalServerException {
        return roleDao.findByUserRole(userRole);
    }

    @Override
    public boolean hasUserRole(long userId, UserRole userRole) {
        return roleDao.hasUserRole(userId, userRole);
    }
}
