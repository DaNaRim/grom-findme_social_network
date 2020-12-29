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

    public static class Builder {

        private final RelationshipValidatorParams params;

        public Builder() {
            params = new RelationshipValidatorParams();
        }

        public Builder withNewStatus(RelationshipStatus newStatus) {
            params.newStatus = newStatus;
            return this;
        }

        public Builder withCurrentStatusFrom(RelationshipStatus currentStatusFrom) {
            params.currentStatusFrom = currentStatusFrom;
            return this;
        }

        public Builder withCurrentStatusTo(RelationshipStatus currentStatusTo) {
            params.currentStatusTo = currentStatusTo;
            return this;
        }

        public Builder withDateModify(Date dateModify) {
            params.dateModify = dateModify;
            return this;
        }

        public Builder withFriends(int friends) {
            params.friends = friends;
            return this;
        }

        public Builder withOutcomeRequests(int outcomeRequests) {
            params.outcomeRequests = outcomeRequests;
            return this;
        }

        public RelationshipValidatorParams build() {
            return params;
        }
    }
}
