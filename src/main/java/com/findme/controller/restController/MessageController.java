package com.findme.controller.restController;

import com.findme.model.Message;
import com.findme.service.MessageService;
import com.findme.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.CREATED)
    public Message save(@RequestParam String text,
                        @RequestParam String userToId) throws Exception {

        return messageService.save(text, Long.parseLong(userToId), SecurityUtil.getAuthorizedUserId());
    }

    @PutMapping("/update")
    public Message update(@RequestParam String id,
                          @RequestParam String text) throws Exception {

        return messageService.update(Long.parseLong(id), text, SecurityUtil.getAuthorizedUserId());
    }

    @DeleteMapping("/deleteMessages")
    public void deleteMessages(@RequestParam String[] ids) throws Exception {

        List<Long> ids0 = Stream.of(ids)
                .mapToLong(Long::parseLong)
                .boxed()
                .collect(Collectors.toList());

        messageService.delete(ids0, SecurityUtil.getAuthorizedUserId());
    }

    @DeleteMapping("/deleteChat")
    public void deleteChat(@RequestParam String userToId) throws Exception {

        messageService.deleteChat(Long.parseLong(userToId), SecurityUtil.getAuthorizedUserId());
    }

    @GetMapping("/getMessages")
    public List<Message> getMessagesByUserId(@RequestParam String userToId,
                                             @RequestParam(defaultValue = "0") String startFrom) throws Exception {

        return messageService.getMessagesWithUser(
                Long.parseLong(userToId),
                SecurityUtil.getAuthorizedUserId(),
                Long.parseLong(startFrom));
    }

}
