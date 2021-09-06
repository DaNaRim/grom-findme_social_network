package com.findme.dao;

import com.findme.model.Message;

import javax.persistence.NoResultException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MessageDaoImpl extends Dao<Message> implements MessageDao {

    private static final String QUERY_DELETE_BY_IDS =
            "DELETE FROM Message"
                    + " WHERE id IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_DELETE_BY_USERS_IDS =
            "DELETE FROM Message"
                    + " WHERE user_from = :" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID
                    + "         AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + "    OR user_from = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + "         AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID;

    private static final String QUERY_SET_DATE_READ =
            "UPDATE Message"
                    + "   SET date_read = CURRENT_TIMESTAMP"
                    + " WHERE id IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_FIND_BY_USER_IDS =
            "SELECT * FROM Message"
                    + "  WHERE user_from = :" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID
                    + "          AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + "     OR user_from = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + "          AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID
                    + "  ORDER BY date_sent"
                    + " OFFSET :" + MessageDaoImpl.ATTRIBUTE_START_FROM
                    + "  LIMIT " + MessageDaoImpl.ATTRIBUTE_RESULT_LIMIT;

    private static final String QUERY_FIND_USER_FROM_IDS_BY_IDS =
            "SELECT user_from"
                    + "  FROM Message"
                    + " WHERE id IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID
                    + " GROUP BY user_from";

    private static final String QUERY_IS_EXISTS =
            "SELECT EXISTS("
                    + "SELECT 1 FROM Message"
                    + " WHERE id = :" + MessageDaoImpl.ATTRIBUTE_ID
                    + " )";

    private static final String QUERY_ARE_EXISTS =
            "SELECT COUNT(*)"
                    + "  FROM Message"
                    + " WHERE id IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_FIND_USER_FROM_BY_ID =
            "SELECT user_from"
                    + "  FROM Message"
                    + " WHERE id = :" + MessageDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_DATE_READ_BY_ID =
            "SELECT date_read"
                    + "  FROM Message"
                    + " WHERE id = :" + MessageDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_DATE_READ_BY_IDS =
            "SELECT date_read"
                    + "  FROM Message"
                    + " WHERE id IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_LIST_ID = "listId";
    private static final String ATTRIBUTE_USER_FROM_ID = "userFromId";
    private static final String ATTRIBUTE_USER_TO_ID = "userToId";

    private static final String ATTRIBUTE_START_FROM = "startFrom";
    private static final int ATTRIBUTE_RESULT_LIMIT = 20;


    public MessageDaoImpl() {
        super(Message.class);
    }

    @Override
    public Message save(Message message) {

        message.setDateSent(new Date());
        return super.save(message);
    }

    @Override
    public Message update(Message message) {

        message.setDateEdited(new Date());
        return super.update(message);
    }

    @Override
    public void updateDateReadByIds(List<Long> ids) {

        em.createNativeQuery(QUERY_SET_DATE_READ)
                .setParameter(ATTRIBUTE_LIST_ID, ids)
                .executeUpdate();
    }

    @Override
    public void deleteByIds(List<Long> ids) {

        em.createNativeQuery(QUERY_DELETE_BY_IDS)
                .setParameter(ATTRIBUTE_LIST_ID, ids)
                .executeUpdate();
    }

    @Override
    public void deleteByUsersIds(long userOneId, long UserTwoId) {

        em.createNativeQuery(QUERY_DELETE_BY_USERS_IDS)
                .setParameter(ATTRIBUTE_USER_FROM_ID, userOneId)
                .setParameter(ATTRIBUTE_USER_TO_ID, UserTwoId)
                .executeUpdate();
    }

    @Override
    public List<Message> findByUserIds(long userOneId, long userTwoId, long startFrom) {

        List<Message> messages = em.createNativeQuery(QUERY_FIND_BY_USER_IDS, Message.class)
                .setParameter(ATTRIBUTE_USER_FROM_ID, userOneId)
                .setParameter(ATTRIBUTE_USER_TO_ID, userTwoId)
                .setParameter(ATTRIBUTE_START_FROM, startFrom)
                .getResultList();

        return messages == null ? new ArrayList<>() : messages;
    }

    @Override
    public boolean areMessagesBelongUser(List<Long> messagesIds, Long userId) {

        List<Integer> userIds = em.createNativeQuery(QUERY_FIND_USER_FROM_IDS_BY_IDS)
                .setParameter(ATTRIBUTE_LIST_ID, messagesIds)
                .getResultList();

        return userIds.size() == 1 && userIds.get(0).longValue() == userId;
    }

    @Override
    public Long findUserFromById(long messageId) {
        try {
            Integer userFrom = (Integer) em.createNativeQuery(QUERY_FIND_USER_FROM_BY_ID)
                    .setParameter(ATTRIBUTE_ID, messageId)
                    .getSingleResult();

            return userFrom.longValue();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Date findDateReadById(long messageId) {
        try {
            return (Date) em.createNativeQuery(QUERY_FIND_DATE_READ_BY_ID)
                    .setParameter(ATTRIBUTE_ID, messageId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public boolean areMessagesRead(List<Long> ids) {

        List<Date> datesRead = em.createNativeQuery(QUERY_FIND_DATE_READ_BY_IDS)
                .setParameter(ATTRIBUTE_LIST_ID, ids)
                .getResultList();

        return !datesRead.stream().allMatch(Objects::isNull);
    }

    @Override
    public boolean isMessageMissing(long id) {

        return !(boolean) em.createNativeQuery(QUERY_IS_EXISTS)
                .setParameter(ATTRIBUTE_ID, id)
                .getSingleResult();
    }

    @Override
    public boolean areMessagesMissing(List<Long> ids) {

        if (ids.isEmpty()) return true;

        BigInteger countExistMessages = (BigInteger) em.createNativeQuery(QUERY_ARE_EXISTS)
                .setParameter(ATTRIBUTE_LIST_ID, ids)
                .getSingleResult();

        return countExistMessages.intValue() != ids.size();
    }
}
