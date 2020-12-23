package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.model.RelationshipStatus;

import static com.findme.model.RelationshipStatus.*;

public class RequestedValidator extends RelationshipValidator {

    public RequestedValidator(RelationshipValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public void checkParams(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == RelationshipStatus.REQUESTED) {

            if (params.getCurrentStatusFrom() == REQUESTED) {
                throw new BadRequestException("You already sent request");

            } else if (params.getCurrentStatusFrom() == FRIENDS) {
                throw new BadRequestException("You already friends");

            } else if (params.getCurrentStatusFrom() == REJECTED) {
                throw new BadRequestException("Can`t send friend request again because user has rejected your request");

            } else if (params.getCurrentStatusTo() == REQUESTED) {
                throw new BadRequestException("Cant sent request to user that sent request to you");

            } else if (params.getOutcomeRequests() > 10) {
                throw new BadRequestException("To many outcome requests");

            } else if (params.getFriends() > 100) {
                throw new BadRequestException("You must have less that 100 friends");
            }
        }
    }
}
