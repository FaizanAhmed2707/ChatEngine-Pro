package com.chat.app_realtime_chat.repository;

import com.chat.app_realtime_chat.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Gets the absolute latest 50 messages (for initial load)
    List<ChatMessage> findTop50ByOrderByIdDesc();

    // Gets the 50 messages immediately BEFORE the given ID (for scrolling up)
    List<ChatMessage> findTop50ByIdLessThanOrderByIdDesc(Long id);
}