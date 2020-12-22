package com.findme.validator;

import com.findme.exception.BadRequestException;

import static com.findme.model.RelationshipStatus.REJECTED;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class BaseUpdateValidator extends RelationshipValidator {

    public BaseUpdateValidator(RelationshipValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public void check(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getCurrentStatusFrom() == null && params.getCurrentStatusTo() == null) {
            throw new BadRequestException("Relationship is not created. Can`t update");

        } else if (params.getNewStatus() == REQUESTED) {
            throw new BadRequestException("Can`t add relationship in update method");

        } else if (params.getNewStatus() == params.getCurrentStatusFrom() && params.getCurrentStatusFrom() != REJECTED) {
            throw new BadRequestException("Can`t update to the same status");
        }
    }
}
