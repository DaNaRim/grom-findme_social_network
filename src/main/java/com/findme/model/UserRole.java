package com.findme.model;

public enum UserRole {
    USER,
    ADMIN,
    SUPER_ADMIN;


    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
