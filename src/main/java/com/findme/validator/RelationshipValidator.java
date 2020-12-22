package com.findme.validator;

import com.findme.exception.BadRequestException;

public abstract class RelationshipValidator {

    private final RelationshipValidator nextValidator;

    public RelationshipValidator(RelationshipValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public abstract void check(RelationshipValidatorParams params) throws BadRequestException;

    public void checkNext(RelationshipValidatorParams params) throws BadRequestException {
        if (nextValidator != null) nextValidator.check(params);
    }
}
