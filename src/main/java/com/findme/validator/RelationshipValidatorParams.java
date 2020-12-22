package com.findme.validator;

import com.findme.model.RelationshipStatus;

import java.util.Date;

public class RelationshipValidatorParams {

    private final RelationshipStatus newStatus;
    private final RelationshipStatus currentStatusFrom;
    private final RelationshipStatus currentStatusTo;
    private final Date dateModify;
    private final int friends;
    private final int outcomeRequests;

    public RelationshipValidatorParams(RelationshipStatus newStatus, RelationshipStatus currentStatusFrom,
                                       RelationshipStatus currentStatusTo, Date dateModify, int friends,
                                       int outcomeRequests) {
        this.newStatus = newStatus;
        this.currentStatusFrom = currentStatusFrom;
        this.currentStatusTo = currentStatusTo;
        this.dateModify = dateModify;
        this.friends = friends;
        this.outcomeRequests = outcomeRequests;
    }

    public RelationshipStatus getNewStatus() {
        return newStatus;
    }

    public RelationshipStatus getCurrentStatusFrom() {
        return currentStatusFrom;
    }

    public RelationshipStatus getCurrentStatusTo() {
        return currentStatusTo;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public int getFriends() {
        return friends;
    }

    public int getOutcomeRequests() {
        return outcomeRequests;
    }
}
