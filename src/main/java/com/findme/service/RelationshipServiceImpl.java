package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.*;
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
            throws ServiceException, InternalServerException {

        Relationship relationship = validateAddRelationship(userFromId, userToId);
        relationship = relationshipDao.save(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }


    public Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws ServiceException, InternalServerException {

        Relationship relationship = validateUpdateRelationship(userFromId, userToId, status);

        relationship = relationshipDao.update(relationship);

        userService.updateDateLastActive(userFromId);

        return relationship;
    }

    public List<Relationship> getIncomeRequests(long userId) throws ServiceException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getIncomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }

        return relationships;
    }

    public List<Relationship> getOutcomeRequests(long userId) throws ServiceException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getOutcomeRequests(userId);

        if (relationships == null) {
            throw new NotFoundException("There are no requests");
        }

        return relationships;
    }

    private Relationship validateAddRelationship(long userFromId, long userToId)
            throws ServiceException, InternalServerException {
        Relationship relationship = validateRelationship(userFromId, userToId);

        RelationshipStatus currentStatus = relationshipDao.getCurrentStatus(userFromId, userToId);

        if (currentStatus != RelationshipStatus.NEVER_FRIENDS) {
            throw new BadRequestException("Relationship already exists");
        }

        relationship.setStatus(RelationshipStatus.REQUEST_HAS_BEEN_SENT);
        return relationship;
    }

    private Relationship validateUpdateRelationship(long userFromId, long userToId, RelationshipStatus status)
            throws ServiceException, InternalServerException {

        Relationship relationship = validateRelationship(userFromId, userToId);

        if (!relationshipDao.isRelationshipExists(userFromId, userToId)) {

            if (!relationshipDao.isRelationshipExists(userToId, userFromId)) {
                throw new NotFoundException("You have no relationship to renew");
            }

        } else {
            RelationshipStatus currentStatus = relationshipDao.getCurrentStatus(userFromId, userToId);

            if (currentStatus == RelationshipStatus.REQUEST_REJECTED) {
                throw new NoAccessException("user has rejected your request");
            }
            if (currentStatus == status) {
                throw new BadRequestException("Can`t update to the same status");
            }
        }

        if (status == RelationshipStatus.NEVER_FRIENDS) {
            throw new BadRequestException("You can`t set relationship to NEVER_FRIENDS");
        }

        relationship.setStatus(status);
        return relationship;
    }

    //returns Relationship with null status
    private Relationship validateRelationship(long userFromId, long userToId)
            throws ServiceException, InternalServerException {

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
