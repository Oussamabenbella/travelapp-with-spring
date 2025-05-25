package com.example.travelappv2.repository;

import com.example.travelappv2.entity.ChatConversation;
import com.example.travelappv2.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByConversationOrderByTimestampAsc(ChatConversation conversation);
}
