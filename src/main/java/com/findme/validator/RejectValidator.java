package com.findme.validator;

import com.findme.exception.BadRequestException;

import static com.findme.model.RelationshipStatus.REJECTED;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class RejectValidator extends RelationshipValidator {

    @Override
    public void checkParams(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == REJECTED && params.getCurrentStatusTo() != REQUESTED) {

            throw new BadRequestException("Can`t reject request because user don`t sent request");
        }
    }
}
