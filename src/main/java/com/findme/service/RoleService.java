package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.UserRole;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    void addRoleByUserId(long userId, UserRole userRole) throws BadRequestException, InternalServerException;

    void removeRoleByUserId(long userId, UserRole userRole) throws BadRequestException, InternalServerException;

    Role findByUserRole(UserRole userRole) throws InternalServerException;

    boolean hasUserRole(long userId, UserRole userRole);
}
