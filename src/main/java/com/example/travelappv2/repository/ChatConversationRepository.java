package com.example.travelappv2.repository;

import com.example.travelappv2.entity.ChatConversation;
import com.example.travelappv2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    List<ChatConversation> findByUserOrderByUpdatedAtDesc(User user);
}
