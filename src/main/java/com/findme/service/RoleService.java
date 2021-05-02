package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.RoleName;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    void addRoleByUserId(long userId, RoleName roleName) throws BadRequestException, InternalServerException;

    void removeRoleByUserId(long userId, RoleName roleName) throws BadRequestException, InternalServerException;

    Role findByUserRole(RoleName roleName) throws InternalServerException;

    boolean hasUserRole(long userId, RoleName roleName);
}
