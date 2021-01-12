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

    private static final String FIND_BY_USERS_QUERY = "SELECT * FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId OR USER_FROM = :userToId AND USER_TO = :userFromId";
    private static final String FIND_STATUS_BY_USERS_QUERY = "SELECT STATUS FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId OR USER_FROM = :userToId AND USER_TO = :userFromId";
    private static final String GET_INCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE STATUS = 'REQUESTED' AND (USER_FROM = :userId OR USER_TO = :userId) AND ACTION_USER_ID != :userId ORDER BY DATE_MODIFY";
    private static final String GET_OUTCOME_REQUESTS_QUERY = "SELECT * FROM RELATIONSHIP WHERE STATUS = 'REQUESTED' AND ACTION_USER_ID = :userId ORDER BY DATE_MODIFY";

    private static final String FIND_ACTION_USER_ID_BY_USERS_QUERY = "SELECT ACTION_USER_ID FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId OR USER_FROM = :userToId AND USER_TO = :userFromId";
    private static final String FIND_DATE_MODIFY_BY_USERS_QUERY = "SELECT DATE_MODIFY FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId OR USER_FROM = :userToId AND USER_TO = :userFromId";
    private static final String COUNT_OUTCOME_REQUESTS_QUERY = "SELECT COUNT(*) FROM RELATIONSHIP WHERE STATUS = 'REQUESTED' AND ACTION_USER_ID = :userId";
    private static final String COUNT_FRIENDS_QUERY = "SELECT COUNT(*) FROM RELATIONSHIP WHERE STATUS = 'FRIENDS' AND (USER_FROM = :userId OR USER_TO = :userId)";

    private static final String FIND_ID_BY_USERS_QUERY = "SELECT ID FROM RELATIONSHIP WHERE USER_FROM = :userFromId AND USER_TO = :userToId OR USER_FROM = :userToId AND USER_TO = :userFromId";

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
            return (Relationship) em.createNativeQuery(FIND_BY_USERS_QUERY, Relationship.class)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
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
            String relationshipStatus = (String) em.createNativeQuery(FIND_STATUS_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
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
            List<Relationship> incomeRequests = em.createNativeQuery(GET_INCOME_REQUESTS_QUERY)
                    .setParameter("userId", userId)
                    .getResultList();

            return incomeRequests == null ? new ArrayList<>() : incomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getIncomeRequests failed: " + e.getMessage());
        }
    }

    @Override
    public List<Relationship> getOutcomeRequests(long userId) throws InternalServerException {
        try {
            List<Relationship> outcomeRequests = em.createNativeQuery(GET_OUTCOME_REQUESTS_QUERY)
                    .setParameter("userId", userId)
                    .getResultList();

            return outcomeRequests == null ? new ArrayList<>() : outcomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getOutcomeRequests failed: " + e.getMessage());
        }
    }


    @Override
    public Long findActionUserId(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer actionUserId = (Integer) em.createNativeQuery(FIND_ACTION_USER_ID_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
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
            return (Date) em.createNativeQuery(FIND_DATE_MODIFY_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
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
            BigInteger outcomeRequests = (BigInteger) em.createNativeQuery(COUNT_OUTCOME_REQUESTS_QUERY)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return outcomeRequests.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countOutcomeRequests failed: " + e.getMessage());
        }
    }

    @Override
    public int countFriends(long userId) throws InternalServerException {
        try {
            BigInteger friends = (BigInteger) em.createNativeQuery(COUNT_FRIENDS_QUERY)
                    .setParameter("userId", userId)
                    .getSingleResult();

            return friends.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countFriends failed: " + e.getMessage());
        }
    }

    private Long findIdByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNativeQuery(FIND_ID_BY_USERS_QUERY)
                    .setParameter("userFromId", userFromId)
                    .setParameter("userToId", userToId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findIdByUsers failed: " + e.getMessage());
        }
    }
}
