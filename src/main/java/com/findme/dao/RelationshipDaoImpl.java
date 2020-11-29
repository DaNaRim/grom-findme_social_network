package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.Date;
import java.util.List;

public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    private static final String GET_CURRENT_STATUS_QUERY = "SELECT STATUS FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId";
    private static final String IS_RELATIONSHIP_EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId)";
    private static final String GET_INCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_TO = :userTo AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";
    private static final String GET_OUTCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFrom AND STATUS = 'REQUEST_HAS_BEEN_SENT' ORDER BY DATE_MODIFY";

    public RelationshipDaoImpl() {
        super(Relationship.class);
    }

    @Override
    public Relationship save(Relationship relationship) throws InternalServerException {

        relationship.setDateModify(new Date());
        return super.save(relationship);
    }

    @Override
    public Relationship update(Relationship relationship) throws InternalServerException {

        relationship.setDateModify(new Date());
        return super.update(relationship);
    }

    public RelationshipStatus getCurrentStatus(long userFromId, long userToId) throws InternalServerException {
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

    public boolean isRelationshipExists(long userFromId, long userToId) throws InternalServerException {
        try {
            return (Boolean) em.createNativeQuery(IS_RELATIONSHIP_EXISTS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findByUserIds failed: " + e.getMessage());
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
