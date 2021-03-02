package com.findme.validator.postValidator;

import com.findme.exception.BadRequestException;

public class TaggedLocationValidator extends PostValidator {

    @Override
    public void checkParams(PostValidatorParams params) throws BadRequestException {

        if (params.getTaggedLocation() != null && params.getTaggedLocation().length() > 128) {
            throw new BadRequestException("TaggedLocation length must be less than 128 characters");
        }
    }
}
