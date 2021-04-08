package com.findme.service;

import com.findme.dao.MessageDao;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Message;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;
    private final UserService userService;
    private final RelationshipService relationshipService;

    @Autowired
    public MessageServiceImpl(MessageDao messageDao, UserService userService, RelationshipService relationshipService) {
        this.messageDao = messageDao;
        this.userService = userService;
        this.relationshipService = relationshipService;
    }

    @Override
    public Message save(String text, Long userToId, long actionUserId)
            throws BadRequestException, InternalServerException {

        validateSave(text, userToId, actionUserId);

        Message message = new Message(text, new User(actionUserId), new User(userToId));
        return messageDao.save(message);
    }

    @Override
    public Message update(Long id, String text, long actionUserId) throws BadRequestException, InternalServerException {

        validateUpdate(id, text, actionUserId);

        Message message = messageDao.findById(id);
        message.setText(text);
        message.setDateEdited(new Date());
        return messageDao.update(message);
    }

    @Override
    public void delete(long id, long actionUserId) throws BadRequestException, InternalServerException {

        validateDelete(id, actionUserId);

        messageDao.delete(messageDao.findById(id));
    }

    @Override
    public List<Message> getMessagesWithUser(long userOneId, long actionUserId, long startFrom)
            throws InternalServerException {

        List<Message> messages = messageDao.findByUserIds(userOneId, actionUserId, startFrom);

        List<Long> messageIds = new ArrayList<>();
        for (Message message : messages) {
            if (message.getDateRead() == null && message.getUserTo().getId() == actionUserId) {
                message.setDateRead(new Date());
                messageIds.add(message.getId());
            }
        }
        if (!messageIds.isEmpty()) messageDao.updateDateReadByIds(messageIds);

        return messages;
    }

    private void validateMessageFields(String text, Long userToId) throws BadRequestException, InternalServerException {

        if (text.isEmpty() || userToId == null) {
            throw new BadRequestException("Text and userTo are required fields");

        } else if (text.length() > 140) {
            throw new BadRequestException("Message length must be less than 140 characters");

        } else if (userService.isUserMissing(userToId)) {
            throw new BadRequestException("UserTo id filed incorrect");
        }
    }


    private void validateSave(String text, Long userToId, long actionUserId)
            throws BadRequestException, InternalServerException {

        validateMessageFields(text, userToId);

        if (!relationshipService.areUsersFriends(userToId, actionUserId)) {
            throw new BadRequestException("You can send messages only to your friends");
        }
    }

    private void validateUpdate(long id, String text, long actionUserId)
            throws BadRequestException, InternalServerException {

        if (text.isEmpty() || text.length() > 140) {
            throw new BadRequestException("Message must be not null and length must be less than 140 characters");

        } else if (messageDao.isMessageMissing(id)) {
            throw new BadRequestException("Message id filed incorrect");

        } else if (messageDao.findUserFromById(id) != actionUserId) {
            throw new BadRequestException("You can`t change not your message");

        } else if (messageDao.findDateReadById(id) != null) {
            throw new BadRequestException("You can`t change message because user read it");
        }
    }

    private void validateDelete(long id, long actionUserId) throws BadRequestException, InternalServerException {

        if (messageDao.isMessageMissing(id)) {
            throw new BadRequestException("Message id filed incorrect");

        } else if (messageDao.findUserFromById(id) != actionUserId) {
            throw new BadRequestException("You can`t delete not your message");

        } else if (messageDao.findDateReadById(id) != null) {
            throw new BadRequestException("You can`t delete message because user read it");
        }
    }
}
