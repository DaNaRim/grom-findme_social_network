package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Relationship;
import com.findme.model.RelationshipStatus;
import org.hibernate.HibernateException;

import javax.persistence.MappedSuperclass;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.findme.model.RelationshipStatus.CANCELED;
import static com.findme.model.RelationshipStatus.DELETED;
import static com.findme.model.RelationshipStatus.REQUESTED;

@MappedSuperclass
@NamedNativeQueries({
        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_FIND_BY_USERS,
                query = "SELECT * FROM Relationship"
                        + " WHERE user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID,
                resultClass = Relationship.class),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_FIND_STATUS_BY_USERS,
                query = "SELECT status FROM Relationship"
                        + " WHERE user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT * FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id != :" + RelationshipDaoImpl.ATTRIBUTE_ACTION_USER_ID
                        + " ORDER BY date_modify",
                resultClass = Relationship.class),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT * FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id = :" + RelationshipDaoImpl.ATTRIBUTE_ACTION_USER_ID
                        + " ORDER BY date_modify",
                resultClass = Relationship.class),


        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_FIND_ACTION_USER_ID_BY_USERS,
                query = "SELECT action_user_id FROM Relationship"
                        + " WHERE user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_FIND_DATE_MODIFY_BY_USERS,
                query = "SELECT date_modify FROM Relationship"
                        + " WHERE user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID,
                query = "SELECT COUNT(*) FROM Relationship"
                        + " WHERE status = 'REQUESTED'"
                        + " AND action_user_id = :" + RelationshipDaoImpl.ATTRIBUTE_ACTION_USER_ID),

        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_COUNT_FRIENDS_BY_USER_ID,
                query = "SELECT COUNT(*) FROM Relationship"
                        + " WHERE status = 'FRIENDS'"
                        + " AND (user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_ID
                        + " OR user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_ID + ")"),


        @NamedNativeQuery(name = RelationshipDaoImpl.QUERY_FIND_ID_BY_USERS,
                query = "SELECT id FROM Relationship"
                        + " WHERE user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " OR user_from = :" + RelationshipDaoImpl.ATTRIBUTE_USER_TO_ID
                        + " AND user_to = :" + RelationshipDaoImpl.ATTRIBUTE_USER_FROM_ID)
})
public class RelationshipDaoImpl extends Dao<Relationship> implements RelationshipDao {

    public static final String QUERY_FIND_BY_USERS = "findByUsers";
    public static final String QUERY_FIND_STATUS_BY_USERS = "findStatusByUsers";
    public static final String QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID = "getIncomeRequestsByActionUserId";
    public static final String QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID = "getOutcomeRequestsByActionUserId";

    public static final String QUERY_FIND_ACTION_USER_ID_BY_USERS = "findActionUserIdByUsers";
    public static final String QUERY_FIND_DATE_MODIFY_BY_USERS = "findDateModifyByUsers";
    public static final String QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID = "countOutcomeRequestsByActionUserId";
    public static final String QUERY_COUNT_FRIENDS_BY_USER_ID = "countFriendsByUserId";

    public static final String QUERY_FIND_ID_BY_USERS = "findIdByUsers";


    public static final String ATTRIBUTE_USER_ID = "userId"; //userFrom and userTo
    public static final String ATTRIBUTE_USER_FROM_ID = "userFromId";
    public static final String ATTRIBUTE_USER_TO_ID = "UserToId";
    public static final String ATTRIBUTE_ACTION_USER_ID = "ActionUserId";

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
            return em.createNamedQuery(QUERY_FIND_BY_USERS, Relationship.class)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findByUsers failed", e);
        }
    }

    @Override
    public RelationshipStatus findStatus(long userFromId, long userToId) throws InternalServerException {
        try {
            String relationshipStatus = (String) em.createNamedQuery(QUERY_FIND_STATUS_BY_USERS)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return RelationshipStatus.valueOf(relationshipStatus);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findStatus failed", e);
        }
    }

    @Override
    public List<Relationship> getIncomeRequests(long userId) throws InternalServerException {
        try {
            List<Relationship> incomeRequests =
                    em.createNamedQuery(QUERY_GET_INCOME_REQUESTS_BY_ACTION_USER_ID, Relationship.class)
                            .setParameter(ATTRIBUTE_ACTION_USER_ID, userId)
                            .getResultList();

            return incomeRequests == null ? new ArrayList<>() : incomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getIncomeRequests failed", e);
        }
    }

    @Override
    public List<Relationship> getOutcomeRequests(long userId) throws InternalServerException {
        try {
            List<Relationship> outcomeRequests =
                    em.createNamedQuery(QUERY_GET_OUTCOME_REQUESTS_BY_ACTION_USER_ID, Relationship.class)
                            .setParameter(ATTRIBUTE_ACTION_USER_ID, userId)
                            .getResultList();

            return outcomeRequests == null ? new ArrayList<>() : outcomeRequests;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.getOutcomeRequests failed", e);
        }
    }


    @Override
    public Long findActionUserId(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer actionUserId = (Integer) em.createNamedQuery(QUERY_FIND_ACTION_USER_ID_BY_USERS)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return actionUserId.longValue();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findActionUserId failed", e);
        }
    }

    @Override
    public Date findDateModify(long userFromId, long userToId) throws InternalServerException {
        try {
            return (Date) em.createNamedQuery(QUERY_FIND_DATE_MODIFY_BY_USERS)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findDateModify failed", e);
        }
    }

    @Override
    public int countOutcomeRequests(long userId) throws InternalServerException {
        try {
            BigInteger outcomeRequests =
                    (BigInteger) em.createNamedQuery(QUERY_COUNT_OUTCOME_REQUESTS_BY_ACTION_USER_ID)
                            .setParameter(ATTRIBUTE_ACTION_USER_ID, userId)
                            .getSingleResult();

            return outcomeRequests.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countOutcomeRequests failed", e);
        }
    }

    @Override
    public int countFriends(long userId) throws InternalServerException {
        try {
            BigInteger friends = (BigInteger) em.createNamedQuery(QUERY_COUNT_FRIENDS_BY_USER_ID)
                    .setParameter(ATTRIBUTE_USER_ID, userId)
                    .getSingleResult();

            return friends.intValue();
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.countFriends failed", e);
        }
    }

    private Long findIdByUsers(long userFromId, long userToId) throws InternalServerException {
        try {
            Integer id = (Integer) em.createNamedQuery(QUERY_FIND_ID_BY_USERS)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userFromId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userToId)
                    .getSingleResult();

            return Long.valueOf(id);
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("RelationshipDaoImpl.findIdByUsers failed", e);
        }
    }
}
