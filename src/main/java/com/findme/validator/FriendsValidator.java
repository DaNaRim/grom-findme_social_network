package com.findme.validator;

import com.findme.exception.BadRequestException;

import static com.findme.model.RelationshipStatus.FRIENDS;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class FriendsValidator extends RelationshipValidator {

    @Override
    public void checkParams(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == FRIENDS && params.getCurrentStatusTo() != REQUESTED) {

            throw new BadRequestException("Can`t add a friend because user don`t sent a friend request");
        }
    }
}
