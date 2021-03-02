package com.findme.validator.relationshipValdator;

import com.findme.exception.BadRequestException;

import static com.findme.model.RelationshipStatus.CANCELED;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class CancelValidator extends RelationshipValidator {

    @Override
    public void checkParams(RelationshipValidatorParams params) throws BadRequestException {

        if (params.getNewStatus() == CANCELED) {

            if (params.getCurrentStatus() != REQUESTED
                    || !params.getOldActionUserId().equals(params.getNewActionUserId())) {

                throw new BadRequestException("Can`t cancel your request because you don`t send it");
            }
        }
    }
}
