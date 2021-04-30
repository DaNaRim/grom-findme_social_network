package com.findme.dao;

import com.findme.model.Role;
import com.findme.model.UserRole;

import java.util.List;

public class RoleDaoImpl extends Dao<Role> implements RoleDao {

    private static final String QUERY_IS_USER_ADMIN =
            "SELECT r.user_role FROM Role r"
                    + " JOIN User_role ur ON ur.role_id = r.id"
                    + " WHERE ur.user_id = :" + RoleDaoImpl.ATTRIBUTE_USER_ID;

    private static final String ATTRIBUTE_USER_ID = "userId";

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public boolean isUserAdmin(long userId) {

        List<Role> roles = em.createNativeQuery(QUERY_IS_USER_ADMIN)
                .setParameter(ATTRIBUTE_USER_ID, userId)
                .getResultList();

        return roles.contains(UserRole.ADMIN);
    }
}
