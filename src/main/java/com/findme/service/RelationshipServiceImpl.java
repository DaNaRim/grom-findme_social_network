package com.findme.service;

import com.findme.dao.RelationshipDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.exception.NotFoundException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
import com.findme.validator.relationshipValdator.CancelValidator;
import com.findme.validator.relationshipValdator.DeleteValidator;
import com.findme.validator.relationshipValdator.FriendsValidator;
import com.findme.validator.relationshipValdator.RejectValidator;
import com.findme.validator.relationshipValdator.RelationshipValidator;
import com.findme.validator.relationshipValdator.RelationshipValidatorParams;
import com.findme.validator.relationshipValdator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.findme.model.RelationshipStatus.DELETED;
import static com.findme.model.RelationshipStatus.FRIENDS;
import static com.findme.model.RelationshipStatus.REQUESTED;

@Transactional
public class RelationshipServiceImpl implements RelationshipService {

    private final RelationshipDao relationshipDao;
    private final UserService userService;

    @Autowired
    public RelationshipServiceImpl(RelationshipDao relationshipDao, UserService userService) {
        this.relationshipDao = relationshipDao;
        this.userService = userService;
    }

    @Override
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

        User userFrom = new User(userFromId);
        User userTo = new User(userToId);

        return relationshipDao.save(new Relationship(userFrom, userTo));
    }

    @Override
    public Relationship updateRelationShip(long userFromId, long userToId, RelationshipStatus status)
            throws NotFoundException, BadRequestException, InternalServerException {

        validateUpdateRelationship(userFromId, userToId, status);

        User userFrom = new User(userFromId);
        User userTo = new User(userToId);

        return relationshipDao.update(new Relationship(userFrom, userTo, status));
    }

    @Override
    public List<Relationship> getIncomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getIncomeRequests(userId);

        if (relationships.isEmpty()) {
            throw new NotFoundException("There are no requests");
        }

        return relationships;
    }

    @Override
    public List<Relationship> getOutcomeRequests(long userId) throws NotFoundException, InternalServerException {

        List<Relationship> relationships = relationshipDao.getOutcomeRequests(userId);

        if (relationships.isEmpty()) {
            throw new NotFoundException("There are no requests");
        }

        return relationships;
    }

    @Override
    public boolean areUsersFriends(long userFromId, long userToId) throws InternalServerException {

        RelationshipStatus status = relationshipDao.findStatus(userFromId, userToId);

        return status == FRIENDS;
    }

    private void validateFields(long userFromId, long userToId)
            throws NotFoundException, BadRequestException, InternalServerException {

        if (userFromId == userToId) {
            throw new BadRequestException("You can`t change relationship to yourself");

        } else if (userService.isUserMissing(userToId)) {
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
