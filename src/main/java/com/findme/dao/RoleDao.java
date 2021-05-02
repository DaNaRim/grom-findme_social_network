package com.findme.dao;

import com.findme.model.Role;
import com.findme.model.RoleName;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {

    void addRoleByUserId(long userId, RoleName roleName);

    void removeRoleByUserId(long userId, RoleName roleName);

    Role findByUserRole(RoleName roleName);

    boolean hasUserRole(long userId, RoleName roleName);
}
