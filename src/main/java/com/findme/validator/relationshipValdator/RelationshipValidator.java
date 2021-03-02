package com.findme.validator.relationshipValdator;

import com.findme.exception.BadRequestException;

public abstract class RelationshipValidator {

    private RelationshipValidator nextValidator;

    public RelationshipValidator linkWith(RelationshipValidator nextValidator) {
        return this.nextValidator = nextValidator;
    }

    public void check(RelationshipValidatorParams params) throws BadRequestException {
        checkParams(params);

        if (nextValidator != null) nextValidator.check(params);
    }

    public abstract void checkParams(RelationshipValidatorParams params) throws BadRequestException;
}
