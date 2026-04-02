package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.MessageType;
import com.chat.app_realtime_chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Collections;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController // Make sure this has @RestController so we can use REST endpoints too
public class ChatController {

    @Autowired
    private ChatMessageRepository messageRepository;

    // 1. The WebSocket route (Saves live messages)
    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Only save actual chat messages to the DB, ignore "JOIN" or "LEAVE" events
        if (message.getType() == MessageType.CHAT) {
            messageRepository.save(message);
        }
        return message;
    }

    @GetMapping("/api/messages/history")
    public List<ChatMessage> getChatHistory(@RequestParam(required = false) Long beforeId) {
        List<ChatMessage> messages;

        if (beforeId == null) {
            // Initial load: get the latest 50
            messages = messageRepository.findTop50ByOrderByIdDesc();
        } else {
            // Scroll up load: get the 50 before the cursor
            messages = messageRepository.findTop50ByIdLessThanOrderByIdDesc(beforeId);
        }

        // Reverse the list so chronological order is maintained (oldest at top, newest at bottom)
        Collections.reverse(messages);
        return messages;
    }
}