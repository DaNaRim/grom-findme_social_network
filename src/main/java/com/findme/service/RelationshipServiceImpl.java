package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.validator.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.findme.model.RelationshipStatus.DELETED;
import static com.findme.model.RelationshipStatus.REQUESTED;

public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipDao relationshipDao;
    private final UserService userService;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao, UserService userService) {
        this.relationshipDao = relationshipDao;
        this.userService = userService;
    }

    @Override
    public RelationshipStatus getRelationShipStatus(long userFromId, long userToId) throws InternalServerException {

        RelationshipStatus relationshipStatus = relationshipDao.findStatusByUsers(userFromId, userToId);

        if (relationshipStatus == null) {
            relationshipStatus = DELETED;
        }

        return relationshipStatus;
    }

    @Override
    public Relationship addRelationShip(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateAddRelationship(userFromId, userToId);

        Relationship relationship =
                new Relationship(userService.findById(userFromId), userService.findById(userToId), REQUESTED);

        relationship = relationshipDao.save(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    @Override
    public Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateUpdateRelationship(userFromId, userToId, status);

        Relationship relationship =
                new Relationship(userService.findById(userFromId), userService.findById(userToId), status);

        relationship = relationshipDao.update(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    @Override
    public List<Relationship> getIncomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getIncomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }

        userService.updateDateLastActive(userId);

        return relationships;
    }

    @Override
    public List<Relationship> getOutcomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getOutcomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }

        userService.updateDateLastActive(userId);

        return relationships;
    }

    private void validateFields(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        if (userFromId == userToId) {
            throw new BadRequestException("You can`t change relationship to yourself");

        } else if (!userService.isUserExists(userToId)) {
            throw new NotFoundException("User id filed incorrect");
        }
    }

    private void validateAddRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateFields(userFromId, userToId);

        RelationshipValidator relationshipValidator = new RequestedValidator();

        RelationshipValidatorParams params = new RelationshipValidatorParams.Builder()
                .withNewStatus(REQUESTED)
                .withCurrentStatusFrom(relationshipDao.findStatusByUsers(userFromId, userToId))
                .withCurrentStatusTo(relationshipDao.findStatusByUsers(userToId, userFromId))
                .withOutcomeRequests(relationshipDao.countOutcomeRequests(userFromId))
                .withFriends(relationshipDao.countFriends(userFromId))
                .build();

        relationshipValidator.check(params);
    }

    private void validateUpdateRelationship(long userFromId, long userToId, RelationshipStatus newStatus)
            throws NotFoundException, BadRequestException, InternalServerException {

        ///Possible newStatus: CANCELED, REJECTED, DELETED, FRIENDS

        validateFields(userFromId, userToId);

        RelationshipValidator baseUpdateValidator = new BaseUpdateValidator();

        baseUpdateValidator.linkWith(new CanceledValidator())
                .linkWith(new RejectedValidator())
                .linkWith(new FriendsValidator())
                .linkWith(new DeletedValidator());

        RelationshipValidatorParams params = new RelationshipValidatorParams.Builder()
                .withNewStatus(newStatus)
                .withCurrentStatusFrom(relationshipDao.findStatusByUsers(userFromId, userToId))
                .withCurrentStatusTo(relationshipDao.findStatusByUsers(userToId, userFromId))
                .withDateModify(relationshipDao.getDateModify(userFromId))
                .build();

        baseUpdateValidator.check(params);
    }
}
