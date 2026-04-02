package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.MessageType;
import com.chat.app_realtime_chat.repository.ChatMessageRepository;
import com.chat.app_realtime_chat.service.PresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    @Autowired
    private PresenceService presenceService;

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {

        System.out.println("🚨 SERVER RECEIVED MESSAGE TYPE: " + message.getType() + " FROM: " + message.getSender());

        if (message.getType() == MessageType.JOIN) {
            headerAccessor.getSessionAttributes().put("username", message.getSender());
            presenceService.addUser(message.getSender());
            System.out.println("🚨 ADDED USER TO PRESENCE SERVICE!");
        } else if (message.getType() == MessageType.CHAT) {
            messageRepository.save(message);
        }
        return message;
    }

    @GetMapping("/api/messages/history")
    public List<ChatMessage> getChatHistory(@RequestParam(required = false) Long beforeId) {
        List<ChatMessage> messages;
        if (beforeId == null) {
            messages = messageRepository.findTop50ByOrderByIdDesc();
        } else {
            messages = messageRepository.findTop50ByIdLessThanOrderByIdDesc(beforeId);
        }
        Collections.reverse(messages);
        return messages;
    }
}