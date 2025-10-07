package com.example.graphql.repository;

import com.example.graphql.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    List<ChatRoom> findByIsActiveTrue();
    
    List<ChatRoom> findByRoomTypeAndIsActiveTrue(ChatRoom.RoomType roomType);
    
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.roomType = :roomType AND cr.isActive = true ORDER BY cr.createdAt DESC")
    List<ChatRoom> findActiveRoomsByType(@Param("roomType") ChatRoom.RoomType roomType);
    
    Optional<ChatRoom> findByNameAndIsActiveTrue(String name);
}
