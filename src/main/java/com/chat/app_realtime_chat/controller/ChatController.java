package com.chat.app_realtime_chat.controller;

import com.chat.app_realtime_chat.model.ChatMessage;
import com.chat.app_realtime_chat.model.User;
import com.chat.app_realtime_chat.model.VideoSignal;
import com.chat.app_realtime_chat.repository.ChatMessageRepository;
import com.chat.app_realtime_chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // NEW: We inject the messaging template so we can route messages to specific dynamic channels
    private final SimpMessageSendingOperations messagingTemplate;

    // --- STANDARD TEXT CHAT ---

    @MessageMapping("/sendMessage")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(@Payload ChatMessage message) {
        if (message.getType() == ChatMessage.MessageType.CHAT) {
            chatMessageRepository.save(message);
        }
        return message;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/messages")
    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String username = message.getSender();
        headerAccessor.getSessionAttributes().put("username", username);

        if (!userRepository.existsById(username)) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setAvatarUrl("https://api.dicebear.com/7.x/pixel-art/svg?seed=" + username);
            newUser.setThemeColor("#0084ff");
            userRepository.save(newUser);
        }

        return message;
    }

    // --- NEW: WEBRTC SIGNALING SWITCHBOARD ---

    @MessageMapping("/video/signal")
    public void handleVideoSignal(@Payload VideoSignal signal) {
        // Instead of broadcasting to everyone, we send this strictly to the target's private video channel
        String privateChannel = "/topic/video/" + signal.getTarget();
        messagingTemplate.convertAndSend(privateChannel, signal);
    }

    // --- REST API ---

    @GetMapping("/api/chat/history")
    @ResponseBody
    public List<ChatMessage> getChatHistory() {
        return chatMessageRepository.findAll();
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}