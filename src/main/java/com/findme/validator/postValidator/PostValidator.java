package com.findme.validator.postValidator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;

public abstract class PostValidator {

    private PostValidator nextValidator;

    public PostValidator linkWith(PostValidator nextValidator) {
        return this.nextValidator = nextValidator;
    }

    public void check(PostValidatorParams params) throws BadRequestException, InternalServerException {
        checkParams(params);

        if (nextValidator != null) nextValidator.check(params);
    }

    public abstract void checkParams(PostValidatorParams params) throws BadRequestException, InternalServerException;
}
