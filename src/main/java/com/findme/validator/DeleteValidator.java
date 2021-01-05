package com.findme.validator;

import com.findme.exception.BadRequestException;

import java.util.Calendar;

import static com.findme.model.RelationshipStatus.DELETED;
import static com.findme.model.RelationshipStatus.FRIENDS;

public class DeleteValidator extends RelationshipValidator {

    @Override
    public void checkParams(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == DELETED) {

            Calendar deleteFriendsTimeLimit = Calendar.getInstance();
            deleteFriendsTimeLimit.setTime(params.getDateModify());
            deleteFriendsTimeLimit.add(Calendar.DAY_OF_MONTH, 3);

            if (params.getCurrentStatus() != FRIENDS) {
                throw new BadRequestException("Can`t delete a friend because you are not friends");

            } else if (deleteFriendsTimeLimit.after(Calendar.getInstance())) {
                throw new BadRequestException("To remove a friend, 3 days must pass from the moment of adding a friend");
            }
        }
    }
}
