package com.findme.dao;

import com.findme.exception.InternalServerException;
import com.findme.model.Message;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageDao {

    Message save(Message message) throws InternalServerException;

    Message findById(long id) throws InternalServerException;

    Message update(Message message) throws InternalServerException;

    void delete(Message message) throws InternalServerException;

    void updateDateReadByIds(List<Long> Ids) throws InternalServerException;

    void deleteByIds(List<Long> ids) throws InternalServerException;

    void deleteByUsersIds(long userOneId, long UserTwoId) throws InternalServerException;

    List<Message> findByUserIds(long userOneId, long userTwoId, long startFrom) throws InternalServerException;

    boolean areMessagesBelongUser(List<Long> messagesIds, Long userId) throws InternalServerException;

    Long findUserFromById(long messageId) throws InternalServerException;

    Date findDateReadById(long messageId) throws InternalServerException;

    boolean areMessagesRead(List<Long> ids) throws InternalServerException;

    boolean isMessageMissing(long id) throws InternalServerException;

    boolean areMessagesMissing(List<Long> ids) throws InternalServerException;

}
