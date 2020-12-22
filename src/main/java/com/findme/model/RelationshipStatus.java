package com.findme.model;

public enum RelationshipStatus {
    DELETED,
    REQUESTED,
    CANCELED, //doesn`t load into db
    REJECTED,
    FRIENDS
}
