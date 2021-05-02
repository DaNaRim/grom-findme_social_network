package com.findme.dao;

import com.findme.model.Message;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MessageDao {

    Message save(Message message);

    Message findById(long id);

    Message update(Message message);

    void delete(Message message);

    void updateDateReadByIds(List<Long> Ids);

    void deleteByIds(List<Long> ids);

    void deleteByUsersIds(long userOneId, long UserTwoId);

    List<Message> findByUserIds(long userOneId, long userTwoId, long startFrom);

    boolean areMessagesBelongUser(List<Long> messagesIds, Long userId);

    Long findUserFromById(long messageId);

    Date findDateReadById(long messageId);

    boolean areMessagesRead(List<Long> ids);

    boolean isMessageMissing(long id);

    boolean areMessagesMissing(List<Long> ids);

}
