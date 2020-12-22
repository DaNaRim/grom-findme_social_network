package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.model.RelationshipStatus;

import java.util.Calendar;
import java.util.Date;

public class DeletedValidator extends RelationshipValidator {

    public DeletedValidator(RelationshipValidator nextValidator) {
        super(nextValidator);
    }

    @Override
    public void check(RelationshipValidatorParams params) throws BadRequestException {

        Calendar deleteFriendsTimeLimit = Calendar.getInstance();
        deleteFriendsTimeLimit.setTime(params.getDateModify());
        deleteFriendsTimeLimit.add(Calendar.DAY_OF_MONTH, 3);

        if (params.getNewStatus() == RelationshipStatus.DELETED) {

            if (params.getCurrentStatusFrom() != RelationshipStatus.FRIENDS) {
                throw new BadRequestException("Can`t delete a friend because you are not friends");

            } else if (deleteFriendsTimeLimit.after(new Date())) {
                throw new BadRequestException("To remove a friend, 3 days must pass from the moment of adding a friend");
            }
        }
    }
}
