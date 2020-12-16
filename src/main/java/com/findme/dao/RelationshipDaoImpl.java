package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import com.findme.model.User;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

import static com.findme.model.RelationshipStatus.*;

@Transactional(rollbackFor = {HibernateException.class, InternalServerException.class})
public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    private static final String FIND_BY_USERS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String FIND_STATUS_BY_USERS_QUERY = "SELECT STATUS FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String FIND_ID_BY_USERS_QUERY = "SELECT ID FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String IS_RELATIONSHIP_EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId)";
    private static final String GET_INCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_TO = :userTo AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";
    private static final String GET_OUTCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFrom AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";

    public RelationshipDaoImpl() {
        super(Relationship.class);
    }

    @Override
    public Relationship save(Relationship relationshipFrom) throws InternalServerException {

        User userFrom = relationshipFrom.getUserFrom();
        User userTo = relationshipFrom.getUserTo();

        relationshipFrom.setStatus(REQUESTED);
        relationshipFrom.setDateModify(new Date());

        if (isRelationshipExists(userFrom.getId(), userTo.getId())) {
            relationshipFrom.setId(findIdByUsers(userFrom.getId(), userTo.getId()));

            return super.update(relationshipFrom);
        } else {
            return super.save(relationshipFrom);
        }
    }

    @Override
    public Relationship update(Relationship relationshipFrom) throws InternalServerException {
       /* possible statuses - DELETED, FRIENDS

       userFrom = FRIENDS - makes users friends
       userFrom = DELETED
            currentUserFrom = REQUESTED (cancel userFrom request)
                userTo = null - deleting relationship
                userTo = DELETED - set statuses DELETED
            userTo = REQUESTED (reject userTo request)
                currentUserFrom = null or DELETED - set userTo REJECTED
                currentUserFrom = REJECTED - set statuses DELETED
            userTo = FRIENDS - set statuses DELETED (delete from friends)

        return null if relationship was deleted
        in other case return processed relationshipFrom
        */

        long userIdFrom = relationshipFrom.getUserFrom().getId();
        long userIdTo = relationshipFrom.getUserTo().getId();

        relationshipFrom.setId(findIdByUsers(userIdFrom, userIdTo));

        Relationship relationshipTo = findByUsers(userIdTo, userIdFrom);

        if (relationshipFrom.getStatus() == FRIENDS) {
            relationshipTo.setStatus(FRIENDS);

            relationshipFrom.setDateModify(new Date());
            relationshipTo.setDateModify(new Date());
            super.update(relationshipTo);
            return super.update(relationshipFrom);
        } else { //if (relationshipFrom.getStatus() == DELETED) {

            return downgradeHandling(relationshipFrom, relationshipTo);
        }
    }

    public Relationship findByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            return (Relationship) em.createNativeQuery(FIND_BY_USERS_QUERY, Relationship.class)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getCurrentStatus failed: " + e.getMessage());
        }
    }

    public RelationshipStatus findStatusByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            String relationshipStatus = (String) em.createNativeQuery(FIND_STATUS_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

            return RelationshipStatus.valueOf(relationshipStatus);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findStatusByUsers failed: " + e.getMessage());
        }
    }

    public Long findIdByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(FIND_ID_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getCurrentStatus failed: " + e.getMessage());
        }
    }

    public boolean isRelationshipExists(long userFromId, long userToId) throws InternalServerException {
        try {
            return (boolean) em.createNativeQuery(IS_RELATIONSHIP_EXISTS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.isRelationshipExists failed: " + e.getMessage());
        }
    }

    public List<Relationship> getIncomeRequests(long userId) throws InternalServerException {
        try {
            return em.createNativeQuery(GET_INCOME_REQUESTS_QUERY)
                    .setParameter("userTo", userId)
                    .getResultList();

        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getIncomeRequests failed: " + e.getMessage());
        }
    }

    public List<Relationship> getOutcomeRequests(long userId) throws InternalServerException {
        try {
            return em.createNativeQuery(GET_OUTCOME_REQUESTS_QUERY)
                    .setParameter("userFrom", userId)
                    .getResultList();

        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getOutcomeRequests failed: " + e.getMessage());
        }
    }

    private Relationship downgradeHandling(Relationship relationshipFrom, Relationship relationshipTo)
            throws InternalServerException {

        long userIdFrom = relationshipFrom.getUserFrom().getId();
        long userIdTo = relationshipFrom.getUserTo().getId();

        RelationshipStatus currentStatusFrom = findStatusByUsers(userIdFrom, userIdTo);

        if (currentStatusFrom == REQUESTED) { //cancel userFrom request
            if (relationshipTo == null) {
                delete(relationshipFrom);

                return null;
            } else { //if (relationshipTo.getStatus() == DELETED) {
                relationshipFrom.setStatus(DELETED);
                relationshipFrom.setDateModify(new Date());

                return super.update(relationshipFrom);
            }
        } else if (relationshipTo.getStatus() == REQUESTED) { //reject userTo request

            relationshipTo.setDateModify(new Date());

            if (currentStatusFrom == null || currentStatusFrom == DELETED) {
                relationshipTo.setStatus(REJECTED);

                super.update(relationshipTo);
                return relationshipFrom;
            } else { //if (currentStatusFrom == REQUEST_REJECTED) {
                relationshipFrom.setStatus(DELETED);
                relationshipTo.setStatus(DELETED);
                relationshipFrom.setDateModify(new Date());

                super.update(relationshipTo);
                return super.update(relationshipFrom);
            }
        } else { //if (relationshipTo.getStatus() == FRIENDS) { //delete from friends
            relationshipTo.setStatus(DELETED);

            relationshipFrom.setDateModify(new Date());
            relationshipTo.setDateModify(new Date());
            super.update(relationshipTo);
            return super.update(relationshipFrom);
        }
    }
}
