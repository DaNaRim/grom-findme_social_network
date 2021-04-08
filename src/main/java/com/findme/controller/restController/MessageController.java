package com.findme.controller.restController;

import com.findme.exception.UnauthorizedException;
import com.findme.model.Message;
import com.findme.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(path = "/send")
    @ResponseStatus(HttpStatus.CREATED)
    public Message save(@RequestParam String text,
                        @RequestParam String userToId,
                        @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return messageService.save(text, Long.parseLong(userToId), userId);
    }

    @PutMapping(path = "/update")
    public Message update(@RequestParam String id,
                          @RequestParam String text,
                          @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return messageService.update(Long.parseLong(id), text, userId);
    }

    @DeleteMapping(path = "/delete")
    public void delete(@RequestParam String id,
                       @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        messageService.delete(Long.parseLong(id), userId);
    }

    @GetMapping(path = "/getMessages")
    public List<Message> getMessagesByUserId(@RequestParam String userToId,
                                             @RequestParam(defaultValue = "0") String startFrom,
                                             @SessionAttribute(required = false) Long userId) throws Exception {

        if (userId == null) {
            throw new UnauthorizedException("You must be authorized to do that");
        }

        return messageService.getMessagesWithUser(
                Long.parseLong(userToId),
                userId,
                Long.parseLong(startFrom));
    }
}
