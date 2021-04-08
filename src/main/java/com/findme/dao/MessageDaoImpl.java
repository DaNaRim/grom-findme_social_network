package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Message;
import org.hibernate.HibernateException;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDaoImpl extends Dao<Message> implements MessageDao {

    private static final String QUERY_SET_DATE_READ =
            "UPDATE Message SET date_read = CURRENT_TIMESTAMP WHERE ID IN :" + MessageDaoImpl.ATTRIBUTE_LIST_ID;

    private static final String QUERY_FIND_BY_USER_IDS =
            "SELECT * FROM Message"
                    + " WHERE user_from =:" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID
                    + " AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + " OR user_from = :" + MessageDaoImpl.ATTRIBUTE_USER_TO_ID
                    + " AND user_to = :" + MessageDaoImpl.ATTRIBUTE_USER_FROM_ID
                    + " ORDER BY date_sent"
                    + " OFFSET :" + MessageDaoImpl.ATTRIBUTE_START_FROM
                    + " LIMIT " + MessageDaoImpl.ATTRIBUTE_RESULT_LIMIT;

    private static final String QUERY_IS_EXISTS =
            "SELECT EXISTS(SELECT 1 FROM Message WHERE id = :" + MessageDaoImpl.ATTRIBUTE_ID + " )";

    private static final String QUERY_FIND_USER_FROM_BY_ID =
            "SELECT user_from FROM Message WHERE id =:" + MessageDaoImpl.ATTRIBUTE_ID;

    private static final String QUERY_FIND_DATE_READ_BY_ID =
            "SELECT date_read FROM Message WHERE id =:" + MessageDaoImpl.ATTRIBUTE_ID;


    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_LIST_ID = "listId";
    private static final String ATTRIBUTE_USER_FROM_ID = "userFromId";
    private static final String ATTRIBUTE_USER_TO_ID = "userToId";

    private static final String ATTRIBUTE_START_FROM = "startFrom";
    private static final int ATTRIBUTE_RESULT_LIMIT = 10;


    public MessageDaoImpl() {
        super(Message.class);
    }

    @Override
    public void updateDateReadByIds(List<Long> Ids) throws InternalServerException {
        try {
            em.createNativeQuery(QUERY_SET_DATE_READ)
                    .setParameter(ATTRIBUTE_LIST_ID, Ids)
                    .executeUpdate();

        } catch (HibernateException e) {
            throw new InternalServerException("MessageDaoImpl.updateDateReadByIds failed", e);
        }
    }

    @Override
    public List<Message> findByUserIds(long userOneId, long userTwoId, long startFrom)
            throws InternalServerException {
        try {
            List<Message> messages = em.createNativeQuery(QUERY_FIND_BY_USER_IDS, Message.class)
                    .setParameter(ATTRIBUTE_USER_FROM_ID, userOneId)
                    .setParameter(ATTRIBUTE_USER_TO_ID, userTwoId)
                    .setParameter(ATTRIBUTE_START_FROM, startFrom)
                    .getResultList();

            return messages == null ? new ArrayList<>() : messages;
        } catch (HibernateException e) {
            throw new InternalServerException("MessageDaoImpl.getMessagesByUsers failed", e);
        }
    }


    @Override
    public Long findUserFromById(long messageId) throws InternalServerException {
        try {
            Integer userFrom = (Integer) em.createNativeQuery(QUERY_FIND_USER_FROM_BY_ID)
                    .setParameter(ATTRIBUTE_ID, messageId)
                    .getSingleResult();

            return userFrom.longValue();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("MessageDaoImpl.findUserFromByID failed", e);
        }
    }


    @Override
    public Date findDateReadById(long messageId) throws InternalServerException {
        try {
            return (Date) em.createNativeQuery(QUERY_FIND_DATE_READ_BY_ID)
                    .setParameter(ATTRIBUTE_ID, messageId)
                    .getSingleResult();

        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            throw new InternalServerException("MessageDaoImpl.findDateReadByID failed", e);
        }
    }

    @Override
    public boolean isMessageMissing(long id) throws InternalServerException {
        try {
            return !(boolean) em.createNativeQuery(QUERY_IS_EXISTS)
                    .setParameter(ATTRIBUTE_ID, id)
                    .getSingleResult();

        } catch (HibernateException e) {
            throw new InternalServerException("MessageDaoImpl.isMessageMissing failed", e);
        }
    }
}
