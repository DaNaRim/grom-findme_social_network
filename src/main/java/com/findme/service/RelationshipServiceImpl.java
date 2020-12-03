package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
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
        return relationshipDao.getRelationshipStatus(userFromId, userToId);
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

    private Relationship validateAddRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        Relationship relationship = validateRelationship(userFromId, userToId);

        RelationshipStatus currentStatusFrom = relationshipDao.getRelationshipStatus(userFromId, userToId);
        RelationshipStatus currentStatusTo = relationshipDao.getRelationshipStatus(userToId, userFromId);

        if (currentStatusFrom != RelationshipStatus.NEVER_FRIENDS
                && currentStatusFrom != RelationshipStatus.NOT_FRIENDS
                || currentStatusTo != RelationshipStatus.NEVER_FRIENDS
                && currentStatusTo != RelationshipStatus.NOT_FRIENDS) {
            throw new BadRequestException("Relationship already exists");
        }
        return relationship;
    }

    private Relationship validateUpdateRelationship(long userFromId, long userToId, RelationshipStatus status)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateRelationship(userFromId, userToId);

        if (status == RelationshipStatus.REQUEST_REJECTED) {
            throw new BadRequestException("Can`t reject your own request");
        }

        Relationship relationshipFrom = relationshipDao.findByUsers(userFromId, userToId);

        if (relationshipFrom == null) {
            throw new BadRequestException("Relationship is not created. Can`t update");
        }

        RelationshipStatus currentStatus = relationshipFrom.getStatus();

        if (currentStatus == status) {
            throw new BadRequestException("Can`t update to the same status");
        }
        if (currentStatus == RelationshipStatus.REQUEST_REJECTED) {
            throw new BadRequestException("Can`t update relationship because user has rejected your request");
        }
        if (status == RelationshipStatus.REQUEST_HAS_BEEN_SENT && currentStatus == RelationshipStatus.FRIENDS) {
            throw new BadRequestException("You already friends");
        }

        Relationship relationshipTo = relationshipDao.findByUsers(userToId, userFromId);

        if (status == RelationshipStatus.FRIENDS
                && relationshipTo.getStatus() != RelationshipStatus.REQUEST_HAS_BEEN_SENT) {
            throw new BadRequestException("Can`t add a friend because user don`t sent a friend request");
        }

        if (status == RelationshipStatus.NEVER_FRIENDS) {
            relationshipFrom.setStatus(RelationshipStatus.NOT_FRIENDS);
        } else {
            relationshipFrom.setStatus(status);
        }
        return relationshipFrom;
    }

    /* In the future, we will need users and in order not to re-access the db,
       the method returns them as a relationship with a null status
     */
    private Relationship validateRelationship(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

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

}
