package com.findme.validator;

import com.findme.model.RelationshipStatus;

import java.util.Date;

public class RelationshipValidatorParams {

    private RelationshipStatus newStatus;
    private RelationshipStatus currentStatusFrom;
    private RelationshipStatus currentStatusTo;
    private Date dateModify;
    private int friends;
    private int outcomeRequests;

    public RelationshipStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(RelationshipStatus newStatus) {
        this.newStatus = newStatus;
    }

    public RelationshipStatus getCurrentStatusFrom() {
        return currentStatusFrom;
    }

    public void setCurrentStatusFrom(RelationshipStatus currentStatusFrom) {
        this.currentStatusFrom = currentStatusFrom;
    }

    public RelationshipStatus getCurrentStatusTo() {
        return currentStatusTo;
    }

    public void setCurrentStatusTo(RelationshipStatus currentStatusTo) {
        this.currentStatusTo = currentStatusTo;
    }

    public Date getDateModify() {
        return dateModify;
    }

    public void setDateModify(Date dateModify) {
        this.dateModify = dateModify;
    }

    public int getFriends() {
        return friends;
    }

    public void setFriends(int friends) {
        this.friends = friends;
    }

    public int getOutcomeRequests() {
        return outcomeRequests;
    }

    public void setOutcomeRequests(int outcomeRequests) {
        this.outcomeRequests = outcomeRequests;
    }
}
