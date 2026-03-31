package com.chat.app_realtime_chat.repository;

import com.chat.app_realtime_chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Built-in methods: save(), findAll(), findById(), etc.
}