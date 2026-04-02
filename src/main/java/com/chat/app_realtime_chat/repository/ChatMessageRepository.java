package com.chat.app_realtime_chat.repository;

import com.chat.app_realtime_chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // Spring Boot writes the SQL for this automatically based on the method name!
    // It grabs the most recent 50 messages to load when a user opens the app.
    List<ChatMessage> findTop50ByOrderByTimestampAsc();
}