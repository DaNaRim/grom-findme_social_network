package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
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

    public Relationship getOurRelationshipToUser(long userFromId, long userToId) throws InternalServerException {

        Relationship relationship = relationshipDao.findByUsers(userFromId, userToId);

        if (relationship.getStatus() == null) {
            relationship.setStatus(DELETED);
        }

        return relationship;
    }

    @Override
    public Relationship addRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateAddRelationship(userFromId, userToId);

        User userFrom = userService.findById(userFromId);
        User userTo = userService.findById(userToId);
        Relationship relationship = relationshipDao.save(new Relationship(userFrom, userTo));

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    @Override
    public Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateUpdateRelationship(userFromId, userToId, status);

        User userFrom = userService.findById(userFromId);
        User userTo = userService.findById(userToId);
        Relationship relationship = relationshipDao.update(new Relationship(userFrom, userTo, status));

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

        RelationshipValidator relationshipValidator = new RequestValidator();

        RelationshipValidatorParams params = new RelationshipValidatorParams.Builder()
                .withNewStatus(REQUESTED)
                .withCurrentStatus(relationshipDao.findStatus(userFromId, userToId))
                .withNewActionUserId(userFromId)
                .withOldActionUserId(relationshipDao.findActionUserId(userFromId, userToId))
                .withOutcomeRequests(relationshipDao.countOutcomeRequests(userFromId))
                .withFriends(relationshipDao.countFriends(userFromId))
                .build();

        relationshipValidator.check(params);
    }

    private void validateUpdateRelationship(long userFromId, long userToId, RelationshipStatus newStatus)
            throws NotFoundException, BadRequestException, InternalServerException {

        ///Possible newStatus: CANCELED, REJECTED, DELETED, FRIENDS

        validateFields(userFromId, userToId);

        RelationshipStatus currentStatus = relationshipDao.findStatus(userFromId, userToId);

        if (currentStatus == null) {
            throw new BadRequestException("Relationship is not created. Can`t update");

        } else if (newStatus == REQUESTED) {
            throw new BadRequestException("Can`t add relationship in update method");

        } else if (newStatus == currentStatus) {
            throw new BadRequestException("Can`t update to the same status");
        }

        RelationshipValidator canceledValidator = new CancelValidator();
        canceledValidator.linkWith(new RejectValidator())
                .linkWith(new FriendsValidator())
                .linkWith(new DeleteValidator());

        RelationshipValidatorParams params = new RelationshipValidatorParams.Builder()
                .withNewStatus(newStatus)
                .withCurrentStatus(currentStatus)
                .withNewActionUserId(userFromId)
                .withOldActionUserId(relationshipDao.findActionUserId(userFromId, userToId))
                .withDateModify(relationshipDao.findDateModify(userFromId, userToId))
                .build();

        canceledValidator.check(params);
    }
}
