package com.example.travelappv2.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private ChatConversation conversation;
    
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Enumerated(EnumType.STRING)
    private MessageRole role;
    
    private LocalDateTime timestamp;
    
    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
    
    public enum MessageRole {
        USER, ASSISTANT, SYSTEM
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatConversation getConversation() {
        return conversation;
    }

    public void setConversation(ChatConversation conversation) {
        this.conversation = conversation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageRole getRole() {
        return role;
    }

    public void setRole(MessageRole role) {
        this.role = role;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
