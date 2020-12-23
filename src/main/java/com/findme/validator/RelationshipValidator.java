package com.findme.validator;

import com.findme.exception.BadRequestException;

public abstract class RelationshipValidator {

    private final RelationshipValidator nextValidator;

    public RelationshipValidator(RelationshipValidator nextValidator) {
        this.nextValidator = nextValidator;
    }

    public void check(RelationshipValidatorParams params) throws BadRequestException {
        checkParams(params);

        if (nextValidator != null) nextValidator.check(params);
    }

    public abstract void checkParams(RelationshipValidatorParams params) throws BadRequestException;
}
