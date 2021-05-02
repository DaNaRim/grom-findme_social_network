package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.RoleName;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {

    void addRoleByUserId(long userId, RoleName roleName);

    void removeRoleByUserId(long userId, RoleName roleName);

    Role findByUserRole(RoleName roleName) throws InternalServerException;

    boolean hasUserRole(long userId, RoleName roleName);
}
