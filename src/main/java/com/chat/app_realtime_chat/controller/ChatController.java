package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.MessageType;
import com.chat.app_realtime_chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    // 2. The REST route (Fetches chat history on load)
    @GetMapping("/api/messages/history")
    public List<ChatMessage> getChatHistory() {
        return messageRepository.findTop50ByOrderByTimestampAsc();
    }
}