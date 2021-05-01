package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Role;
import com.findme.model.UserRole;

import javax.persistence.NoResultException;
import java.util.List;

public class RoleDaoImpl extends Dao<Role> implements RoleDao {

    private static final String QUERY_ADD_ROLE_BY_USER_ID =
            "INSERT INTO User_role VALUES ("
                    + RoleDaoImpl.ATTRIBUTE_USER_ID + ", "
                    + "(SELECT id FROM Role WHERE user_role = :" + RoleDaoImpl.ATTRIBUTE_USER_ROLE + " )"
                    + ")";

    private static final String QUERY_REMOVE_ROLE_BY_USER_ID =
            "DELETE FROM User_role"
                    + " WHERE user_id = :" + RoleDaoImpl.ATTRIBUTE_USER_ID
                    + "  AND role_id = "
                    + "(SELECT id FROM Role WHERE user_role = :" + RoleDaoImpl.ATTRIBUTE_USER_ROLE + ")";

    private static final String QUERY_FIND_BY_USER_ROLE =
            "SELECT * FROM Role WHERE user_role = :" + RoleDaoImpl.ATTRIBUTE_USER_ROLE;

    private static final String QUERY_IS_USER_ADMIN =
            "SELECT r.user_role FROM Role r"
                    + " JOIN User_role ur ON ur.role_id = r.id"
                    + " WHERE ur.user_id = :" + RoleDaoImpl.ATTRIBUTE_USER_ID;

    private static final String ATTRIBUTE_USER_ROLE = "userRole";
    private static final String ATTRIBUTE_USER_ID = "userId";

    public RoleDaoImpl() {
        super(Role.class);
    }

    @Override
    public void addRoleByUserId(long userId, UserRole userRole) {

        em.createNativeQuery(QUERY_ADD_ROLE_BY_USER_ID)
                .setParameter(ATTRIBUTE_USER_ID, userId)
                .setParameter(ATTRIBUTE_USER_ROLE, userRole.name())
                .executeUpdate();
    }

    @Override
    public void removeRoleByUserId(long userId, UserRole userRole) {
        em.createNativeQuery(QUERY_REMOVE_ROLE_BY_USER_ID)
                .setParameter(ATTRIBUTE_USER_ID, userId)
                .setParameter(ATTRIBUTE_USER_ROLE, userRole.name())
                .executeUpdate();
    }

    @Override
    public Role findByUserRole(UserRole userRole) throws InternalServerException {
        try {
            return (Role) em.createNativeQuery(QUERY_FIND_BY_USER_ROLE, Role.class)
                    .setParameter(ATTRIBUTE_USER_ROLE, userRole.name())
                    .getSingleResult();

        } catch (NoResultException e) {
            throw new InternalServerException("There is no role with this userRole name", e);
        }
    }

    @Override
    public boolean hasUserRole(long userId, UserRole userRole) {
        List<Role> roles = em.createNativeQuery(QUERY_IS_USER_ADMIN)
                .setParameter(ATTRIBUTE_USER_ID, userId)
                .getResultList();

        return roles.contains(userRole);
    }

}
