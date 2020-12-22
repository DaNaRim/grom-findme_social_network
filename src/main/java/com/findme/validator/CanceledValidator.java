package com.findme.validator;

import com.findme.exception.BadRequestException;

import static com.findme.model.RelationshipStatus.CANCELED;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class CanceledValidator extends RelationshipValidator {

    public CanceledValidator(RelationshipValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public void check(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == CANCELED && params.getCurrentStatusFrom() != REQUESTED) {

            throw new BadRequestException("Can`t cancel your request because you don`t send it");
        }
    }
}
