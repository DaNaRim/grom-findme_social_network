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

@Transactional(rollbackFor = {HibernateException.class, InternalServerException.class})
public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    private static final String GET_CURRENT_STATUS_QUERY = "SELECT STATUS FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String FIND_BY_USERS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
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

        if (!isRelationshipExists(userFrom.getId(), userTo.getId())) {

            super.save(new Relationship(userTo, userFrom, RelationshipStatus.NEVER_FRIENDS, new Date()));
        }

        relationshipFrom.setStatus(RelationshipStatus.REQUEST_HAS_BEEN_SENT);
        relationshipFrom.setDateModify(new Date());
        return super.save(relationshipFrom);
    }

    @Override
    public Relationship update(Relationship relationshipFrom) throws InternalServerException {
        //NOT_FRIENDS (NEVER_FRIENDS), FRIENDS

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
        } else { // if (relationshipFrom.getStatus() == RelationshipStatus.FRIENDS) {
            relationshipTo.setStatus(RelationshipStatus.FRIENDS);

            relationshipFrom.setDateModify(new Date());
            relationshipTo.setDateModify(new Date());
            super.update(relationshipTo);
            return super.update(relationshipFrom);
        }
    }

    public RelationshipStatus getRelationshipStatus(long userFromId, long userToId) throws InternalServerException {
        try {
            String relationshipStatus = (String) em.createNativeQuery(GET_CURRENT_STATUS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

            return RelationshipStatus.valueOf(relationshipStatus);
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
}
