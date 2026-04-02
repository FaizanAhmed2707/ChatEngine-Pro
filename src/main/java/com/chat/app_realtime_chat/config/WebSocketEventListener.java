package com.chat.app_realtime_chat.config;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.MessageType;

// These two imports fix the Red Logger errors
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // We extract the attributes safely to fix the Yellow NullPointerException warning
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();

        // Check if attributes exist and contain the username before trying to read it
        if (sessionAttributes != null && sessionAttributes.containsKey("username")) {
            String username = (String) sessionAttributes.get("username");

            logger.info("User disconnected: {}", username);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);

            messagingTemplate.convertAndSend("/topic/messages", chatMessage);
        }
    }
}