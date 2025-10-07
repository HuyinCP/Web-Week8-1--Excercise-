package com.example.graphql.repository;

import com.example.graphql.model.ChatSession;
import com.example.graphql.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    
    Optional<ChatSession> findBySessionId(String sessionId);
    
    List<ChatSession> findByStatus(ChatSession.SessionStatus status);
    
    List<ChatSession> findByCustomerIdAndStatus(Long customerId, ChatSession.SessionStatus status);
    
    List<ChatSession> findBySupportAgentIdAndStatus(Long supportAgentId, ChatSession.SessionStatus status);
    
    List<ChatSession> findByChatRoom(ChatRoom chatRoom);
    
    @Query("SELECT cs FROM ChatSession cs WHERE cs.status = 'WAITING' ORDER BY cs.createdAt ASC")
    List<ChatSession> findWaitingSessions();
    
    @Query("SELECT cs FROM ChatSession cs WHERE cs.supportAgentId = :agentId AND cs.status IN ('ACTIVE', 'WAITING')")
    List<ChatSession> findActiveSessionsByAgent(@Param("agentId") Long agentId);
    
    @Query("SELECT cs FROM ChatSession cs WHERE cs.customerId = :customerId AND cs.status = 'ACTIVE'")
    Optional<ChatSession> findActiveSessionByCustomer(@Param("customerId") Long customerId);
}
