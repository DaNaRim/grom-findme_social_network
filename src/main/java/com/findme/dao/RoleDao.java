package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.UserRole;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {

    void addRoleByUserId(long userId, UserRole userRole);

    void removeRoleByUserId(long userId, UserRole userRole);

    Role findByUserRole(UserRole userRole) throws InternalServerException;

    boolean hasUserRole(long userId, UserRole userRole);
}
