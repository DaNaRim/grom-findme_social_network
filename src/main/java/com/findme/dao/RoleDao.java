package com.findme.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao {

    boolean isUserAdmin(long userId);
}
