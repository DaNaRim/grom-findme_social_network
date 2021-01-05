package com.findme.validator;

import com.findme.model.RelationshipStatus;

import java.util.Date;

public class RelationshipValidatorParams {

    private RelationshipStatus newStatus;
    private RelationshipStatus currentStatus;
    private Long newActionUserId;
    private Long oldActionUserId;
    private Date dateModify;
    private int friends;
    private int outcomeRequests;

    public RelationshipStatus getNewStatus() {
        return newStatus;
    }

    public RelationshipStatus getCurrentStatus() {
        return currentStatus;
    }

    public Long getNewActionUserId() {
        return newActionUserId;
    }

    public Long getOldActionUserId() {
        return oldActionUserId;
    }

    public Date getDateModify() {
        return new Date(dateModify.getTime());
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

        public Builder withCurrentStatus(RelationshipStatus currentStatus) {
            params.currentStatus = currentStatus;
            return this;
        }

        public Builder withNewActionUserId(Long newActionUserId) {
            params.newActionUserId = newActionUserId;
            return this;
        }

        public Builder withOldActionUserId(Long oldActionUserId) {
            params.oldActionUserId = oldActionUserId;
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
