package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.findme.model.RelationshipStatus.*;

public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipDao relationshipDao;
    private final UserService userService;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao, UserService userService) {
        this.relationshipDao = relationshipDao;
        this.userService = userService;
    }

    public Relationship addRelationShip(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        Relationship relationship = validateAddRelationship(userFromId, userToId);
        relationship = relationshipDao.save(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    public RelationshipStatus getRelationShipStatus(long userFromId, long userToId) throws InternalServerException {
        return relationshipDao.findStatusByUsers(userFromId, userToId);
    }

    public Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws NotFoundException, BadRequestException, InternalServerException {

        Relationship relationship = validateUpdateRelationship(userFromId, userToId, status);
        relationship = relationshipDao.update(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    public List<Relationship> getIncomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getIncomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }
        userService.updateDateLastActive(userId);

        return relationships;
    }

    public List<Relationship> getOutcomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getOutcomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }
        userService.updateDateLastActive(userId);

        return relationships;
    }

    private Relationship validateRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {
        /* In the future, we will need users and in order not to re-access the db,
            the method returns them as a relationship with a null status
        */
        if (userFromId == userToId) {
            throw new BadRequestException("you can`t change relationship to yourself");
        }
        User userFrom = userService.findById(userFromId);
        User userTo = userService.findById(userToId);

        if (userFrom == null || userTo == null) {
            throw new NotFoundException("Can`t found one of users");
        }

        return new Relationship(userFrom, userTo);
    }

    private Relationship validateAddRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        Relationship relationship = validateRelationship(userFromId, userToId);

        RelationshipStatus currentStatusFrom = relationshipDao.findStatusByUsers(userFromId, userToId);

        if (currentStatusFrom != NEVER_FRIENDS && currentStatusFrom != NOT_FRIENDS) {
            throw new BadRequestException("Relationship already exists");
        }
        return relationship;
    }

    private Relationship validateUpdateRelationship(long userFromId, long userToId, RelationshipStatus newStatus)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateRelationship(userFromId, userToId);

        if (newStatus == REQUEST_REJECTED) {
            throw new BadRequestException("Can`t reject your own request");
        }
        if (newStatus == REQUEST_HAS_BEEN_SENT) {
            throw new BadRequestException("Can`t add relationship in update method");
        }

        Relationship relationshipFrom = relationshipDao.findByUsers(userFromId, userToId);

        if (relationshipFrom == null) {
            throw new BadRequestException("Relationship is not created. Can`t update");
        }

        RelationshipStatus currentStatus = relationshipFrom.getStatus();

        if (currentStatus == newStatus) {
            throw new BadRequestException("Can`t update to the same status");
        }
        if (currentStatus == REQUEST_REJECTED) {
            throw new BadRequestException("Can`t update relationship because user has rejected your request");
        }

        RelationshipStatus statusTo = relationshipDao.findStatusByUsers(userToId, userFromId);

        if (newStatus == FRIENDS && statusTo != REQUEST_HAS_BEEN_SENT) {
            throw new BadRequestException("Can`t add a friend because user don`t sent a friend request");
        }

        if (newStatus == NEVER_FRIENDS) {
            relationshipFrom.setStatus(NOT_FRIENDS);
        } else {
            relationshipFrom.setStatus(newStatus);
        }
        return relationshipFrom;
    }
}
