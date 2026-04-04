package com.chat.app_realtime_chat.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(255)") // ADD THIS LINE
    private MessageType type;

    private LocalDateTime timestamp;

    // This automatically sets the exact time the message was saved to the database
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // --- GETTERS & SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}