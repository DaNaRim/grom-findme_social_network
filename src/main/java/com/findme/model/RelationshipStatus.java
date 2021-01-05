package com.findme.model;

public enum RelationshipStatus {
    DELETED,
    REQUESTED,
    CANCELED, //doesn`t load to db
    REJECTED,
    FRIENDS
}
