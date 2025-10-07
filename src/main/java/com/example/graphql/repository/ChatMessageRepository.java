package com.example.graphql.repository;

import com.example.graphql.model.ChatMessage;
import com.example.graphql.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    
    List<ChatMessage> findByChatRoomOrderByCreatedAtAsc(ChatRoom chatRoom);
    
    List<ChatMessage> findByChatRoomAndCreatedAtAfterOrderByCreatedAtAsc(ChatRoom chatRoom, LocalDateTime after);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.isRead = false ORDER BY cm.createdAt ASC")
    List<ChatMessage> findUnreadMessagesByChatRoom(@Param("chatRoom") ChatRoom chatRoom);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.senderType = :senderType ORDER BY cm.createdAt DESC")
    List<ChatMessage> findByChatRoomAndSenderType(@Param("chatRoom") ChatRoom chatRoom, 
                                                  @Param("senderType") ChatMessage.SenderType senderType);
    
    @Query("SELECT COUNT(cm) FROM ChatMessage cm WHERE cm.chatRoom = :chatRoom AND cm.isRead = false")
    Long countUnreadMessagesByChatRoom(@Param("chatRoom") ChatRoom chatRoom);
}
