package com.findme.service;

import com.findme.dao.RoleDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.RoleName;
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
    public void addRoleByUserId(long userId, RoleName roleName)
            throws BadRequestException, InternalServerException {

        if (userService.isUserMissing(userId)) {
            throw new BadRequestException("Missing user with this id");

        } else if (roleDao.hasUserRole(userId, roleName)) {
            throw new BadRequestException("User already has this role");
        }

        roleDao.addRoleByUserId(userId, roleName);
    }

    @Override
    public void removeRoleByUserId(long userId, RoleName roleName)
            throws BadRequestException, InternalServerException {

        if (userService.isUserMissing(userId)) {
            throw new BadRequestException("Missing user with this id");

        } else if (!roleDao.hasUserRole(userId, roleName)) {
            throw new BadRequestException("User hasn't this role");
        }

        roleDao.removeRoleByUserId(userId, roleName);
    }

    @Override
    public Role findByUserRole(RoleName roleName) throws InternalServerException {
        return roleDao.findByUserRole(roleName);
    }

    @Override
    public boolean hasUserRole(long userId, RoleName roleName) {
        return roleDao.hasUserRole(userId, roleName);
    }
}
