package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.findme.model.RelationshipStatus.*;

public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    public RelationshipDaoImpl() {
        super(Relationship.class);
    }

    @Override
    public Relationship save(Relationship relationship) throws InternalServerException {

        long userFromId = relationship.getUserFrom().getId();
        long userToId = relationship.getUserTo().getId();

        relationship.setStatus(REQUESTED);
        relationship.setActionUserId(userFromId);
        relationship.setDateModify(new Date());

        Long relationshipId = findIdByUsers(userFromId, userToId);

        if (relationshipId != null) {
            relationship.setId(relationshipId);

            return super.update(relationship);
        }
        return super.save(relationship);
    }

    @Override
    public Relationship update(Relationship relationship) throws InternalServerException {

        long userFromId = relationship.getUserFrom().getId();
        long userToId = relationship.getUserTo().getId();

        if (relationship.getStatus() == CANCELED) {
            relationship.setStatus(DELETED);
        }

        relationship.setActionUserId(userFromId);
        relationship.setDateModify(new Date());
        relationship.setId(findIdByUsers(userFromId, userToId));

        return super.update(relationship);
    }

    @Override
    public Relationship findByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            return em.createNamedQuery(Relationship.QUERY_FIND_BY_USERS, Relationship.class)
                    .setParameter(Relationship.ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(Relationship.ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findByUsers failed: " + e.getMessage());
        }
    }

    @Override
    public RelationshipStatus findStatus(long userFromId, long userToId) throws InternalServerException {
        try {
            String relationshipStatus = (String) em.createNamedQuery(Relationship.QUERY_FIND_STATUS_BY_USERS)
                    .setParameter(Relationship.ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(Relationship.ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return RelationshipStatus.valueOf(relationshipStatus);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findStatus failed: " + e.getMessage());
        }
    }

    @Override
    public List<Relationship> getIncomeRequests(long userId) throws InternalServerException {
        try {
            List<Relationship> incomeRequests = em.createNamedQuery(Relationship.QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID)
                    .setParameter(Relationship.ATTRIBUTE_ACTION_USER_ID, userId)
                    .getResultList();

            return incomeRequests == null ? new ArrayList<>() : incomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getIncomeRequests failed: " + e.getMessage());
        }
    }

    @Override
    public List<Relationship> getOutcomeRequests(long userId) throws InternalServerException {
        try {
            List<Relationship> outcomeRequests = em.createNamedQuery(Relationship.QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID)
                    .setParameter(Relationship.ATTRIBUTE_ACTION_USER_ID, userId)
                    .getResultList();

            return outcomeRequests == null ? new ArrayList<>() : outcomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getOutcomeRequests failed: " + e.getMessage());
        }
    }


    @Override
    public Long findActionUserId(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer actionUserId = (Integer) em.createNamedQuery(Relationship.QUERY_FIND_ACTION_USER_ID_BY_USERS)
                    .setParameter(Relationship.ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(Relationship.ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return actionUserId.longValue();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findActionUserId failed: " + e.getMessage());
        }
    }

    @Override
    public Date findDateModify(long userFromId, long userToId) throws InternalServerException {
        try {
            return (Date) em.createNamedQuery(Relationship.QUERY_FIND_DATE_MODIFY_BY_USERS)
                    .setParameter(Relationship.ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(Relationship.ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findDateModify failed: " + e.getMessage());
        }
    }

    @Override
    public int countOutcomeRequests(long userId) throws InternalServerException {
        try {
            BigInteger outcomeRequests = (BigInteger) em.createNamedQuery(Relationship.QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID)
                    .setParameter(Relationship.ATTRIBUTE_ACTION_USER_ID, userId)
                    .getSingleResult();

            return outcomeRequests.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countOutcomeRequests failed: " + e.getMessage());
        }
    }

    @Override
    public int countFriends(long userId) throws InternalServerException {
        try {
            BigInteger friends = (BigInteger) em.createNamedQuery(Relationship.QUERY_COUNT_FRIENDS_BY_USER_ID)
                    .setParameter(Relationship.ATTRIBUTE_USER_ID, userId)
                    .getSingleResult();

            return friends.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countFriends failed: " + e.getMessage());
        }
    }

    private Long findIdByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNamedQuery(Relationship.QUERY_FIND_ID_BY_USERS)
                    .setParameter(Relationship.ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(Relationship.ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findIdByUsers failed: " + e.getMessage());
        }
    }
}
