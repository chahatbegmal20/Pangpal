package dev.n7meless.controller;

import dev.n7meless.dto.message.MessageRequest;
import dev.n7meless.dto.message.MessageResponse;
import dev.n7meless.entity.User;
import dev.n7meless.exception.UserNotFoundError;
import dev.n7meless.model.ChatMessage;
import dev.n7meless.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {

    //    @MessageMapping("/chat")
//    @SendTo("/topic/public")
//    public ChatMessage register(@Payload ChatMessage chatMessage,
//                                SimpMessageHeaderAccessor headerAccessor){
//        headerAccessor.getSessionAttributes().put("user_id", chatMessage.getSecondUser());
//        return chatMessage;
//    }
    private final UserService userService;

    @Autowired
    public MessageController(UserService userService) {
        this.userService = userService;
    }

    @MessageMapping("/chat")
    @SendTo("/topic")
    public MessageResponse sendMessage(@Payload MessageRequest messageRequest) {
        User first = userService.getUserById(messageRequest.first_user()).orElseThrow(UserNotFoundError::new);
        User second = userService.getUserById(messageRequest.second_user()).orElseThrow(UserNotFoundError::new);
        ChatMessage.of(first, second, messageRequest.text());
        return new MessageResponse(second.getFirstName(), messageRequest.text());
    }

}
