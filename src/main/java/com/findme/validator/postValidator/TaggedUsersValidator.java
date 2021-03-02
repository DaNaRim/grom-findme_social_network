package com.findme.validator.postValidator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.service.UserService;

public class TaggedUsersValidator extends PostValidator {

    private final UserService userService;

    public TaggedUsersValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void checkParams(PostValidatorParams params) throws BadRequestException, InternalServerException {

        if (params.getTaggedUsersIds() != null) {

            if (params.getTaggedUsersIds().contains(params.getUserPostedId())) {
                throw new BadRequestException("You can`t tag yourself");

            } else if (userService.isUsersMissing(params.getTaggedUsersIds())) {
                throw new BadRequestException("Tagged users ids filed incorrect");
            }
        }
    }
}
