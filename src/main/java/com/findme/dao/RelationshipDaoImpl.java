package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.hibernate.HibernateException;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    private static final String GET_CURRENT_STATUS_QUERY = "SELECT STATUS FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String FIND_BY_USERS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String GET_INCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_TO = :userTo AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";
    private static final String GET_OUTCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFrom AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";

    public RelationshipDaoImpl() {
        super(Relationship.class);
    }

    @Override
    @Transactional
    public Relationship save(Relationship relationshipFrom) throws InternalServerException {

        relationshipFrom.setStatus(RelationshipStatus.REQUEST_HAS_BEEN_SENT);
        relationshipFrom.setDateModify(new Date());

        Relationship relationshipTo = findByUsers(relationshipFrom.getUserTo().getId(),
                relationshipFrom.getUserFrom().getId());

        if (relationshipTo == null) {
            relationshipTo = new Relationship(relationshipFrom.getUserTo(), relationshipFrom.getUserFrom(),
                    RelationshipStatus.NEVER_FRIENDS, new Date());

            super.save(relationshipTo);
        }

        return super.save(relationshipFrom);
    }

    @Override
    @Transactional
    public Relationship update(Relationship relationshipFrom) throws InternalServerException {
        //NOT_FRIENDS, REQUEST_HAS_BEEN_SENT, FRIENDS

        Relationship relationshipTo = findByUsers(relationshipFrom.getUserTo().getId(),
                relationshipFrom.getUserFrom().getId());

        RelationshipStatus currentRelationshipStatusFrom = getRelationshipStatus(relationshipFrom.getUserFrom().getId(),
                relationshipFrom.getUserTo().getId());

        if (relationshipFrom.getStatus() == RelationshipStatus.NOT_FRIENDS) {

            //cancel my request
            if (relationshipTo.getStatus() == RelationshipStatus.NEVER_FRIENDS) {
                delete(relationshipFrom);
                delete(relationshipTo);

                return null;
            } else if (relationshipTo.getStatus() == RelationshipStatus.NOT_FRIENDS) {
                relationshipFrom.setStatus(RelationshipStatus.NOT_FRIENDS);
                relationshipTo.setStatus(RelationshipStatus.NOT_FRIENDS);

                relationshipFrom.setDateModify(new Date());
                relationshipTo.setDateModify(new Date());
                super.update(relationshipTo);
                return super.update(relationshipFrom);

            } else if (relationshipTo.getStatus() == RelationshipStatus.REQUEST_HAS_BEEN_SENT) {
                //rejecting request

                relationshipTo.setStatus(RelationshipStatus.REQUEST_REJECTED);

                if (currentRelationshipStatusFrom == RelationshipStatus.NEVER_FRIENDS) {
                    delete(relationshipFrom);
                    return null;
                } else { //if (currentRelationshipStatusFrom == RelationshipStatus.NOT_FRIENDS) {
                    relationshipFrom.setDateModify(new Date());
                    relationshipTo.setDateModify(new Date());
                    super.update(relationshipTo);
                    return super.update(relationshipFrom);
                }
            } else { //removal from friends
                relationshipTo.setStatus(RelationshipStatus.NOT_FRIENDS);

                relationshipFrom.setDateModify(new Date());
                relationshipTo.setDateModify(new Date());
                super.update(relationshipTo);
                return super.update(relationshipFrom);
            }
        } else if (relationshipFrom.getStatus() == RelationshipStatus.FRIENDS) {
            relationshipTo.setStatus(RelationshipStatus.FRIENDS);

            relationshipFrom.setDateModify(new Date());
            relationshipTo.setDateModify(new Date());
            super.update(relationshipTo);
            return super.update(relationshipFrom);

        } else { //if (relationshipFrom.getStatus() == RelationshipStatus.REQUEST_HAS_BEEN_SENT) {
            relationshipFrom.setDateModify(new Date());
            return super.update(relationshipFrom);
        }
    }

    public RelationshipStatus getRelationshipStatus(long userFromId, long userToId) throws InternalServerException {
        try {
            return (RelationshipStatus) em.createNativeQuery(GET_CURRENT_STATUS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return RelationshipStatus.NEVER_FRIENDS;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getCurrentStatus failed: " + e.getMessage());
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
}
