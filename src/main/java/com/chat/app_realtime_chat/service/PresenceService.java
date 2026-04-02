package com.chat.app_realtime_chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {

    // Thread-safe Set to prevent crashes if 100 people join at the exact same millisecond
    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    public void addUser(String username) {
        activeUsers.add(username);
        broadcastPresence();
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
        broadcastPresence();
    }

    // Sends the updated array of usernames to React
    private void broadcastPresence() {
        messagingTemplate.convertAndSend("/topic/presence", activeUsers);
    }
}