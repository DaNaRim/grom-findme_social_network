package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerException;
import com.findme.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService {

    Message save(String text, Long userToId, long actionUserId) throws BadRequestException, InternalServerException;

    Message update(Long id, String text, long actionUserId) throws BadRequestException, InternalServerException;

    void delete(List<Long> ids, long actionUserId) throws BadRequestException, InternalServerException;

    void deleteChat(long userToId, long actionUserId) throws BadRequestException, InternalServerException;

    List<Message> getMessagesWithUser(long userOneId, long actionUserId, long startFrom) throws InternalServerException;
}
