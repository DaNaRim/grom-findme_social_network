package com.findme.validator.postValidator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.service.UserService;

public class UserPagePostedValidator extends PostValidator {

    private final UserService userService;

    public UserPagePostedValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void checkParams(PostValidatorParams params) throws BadRequestException, InternalServerException {

        if (params.getUserPagePostedId() == null) {
            throw new BadRequestException("UserPagePosted are required field");

        } else if (userService.isUserMissing(params.getUserPagePostedId())) {
            throw new BadRequestException("userPagePosted id filed incorrect");
        }
    }
}
