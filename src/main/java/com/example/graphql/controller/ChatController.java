package com.example.graphql.controller;

import com.example.graphql.model.*;
import com.example.graphql.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    
    private final ChatService chatService;
    
    // WebSocket Message Handlers
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        log.info("Received message: {}", chatMessage.getContent());
        return chatMessage;
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderName());
        return chatMessage;
    }
    
    @MessageMapping("/room.sendMessage")
    public void sendMessageToRoom(@Payload Map<String, Object> payload) {
        try {
            Long chatRoomId = Long.valueOf(payload.get("chatRoomId").toString());
            String content = payload.get("content").toString();
            Long senderId = Long.valueOf(payload.get("senderId").toString());
            String senderName = payload.get("senderName").toString();
            String senderType = payload.get("senderType").toString();
            
            chatService.sendMessage(
                chatRoomId, 
                content, 
                senderId, 
                senderName, 
                ChatMessage.SenderType.valueOf(senderType)
            );
        } catch (Exception e) {
            log.error("Error sending message to room: {}", e.getMessage());
        }
    }
    
    @MessageMapping("/session.create")
    public void createSession(@Payload Map<String, Object> payload) {
        try {
            Long customerId = Long.valueOf(payload.get("customerId").toString());
            String customerName = payload.get("customerName").toString();
            
            ChatSession session = chatService.createChatSession(customerId, customerName);
            chatService.notifyNewSession(session);
        } catch (Exception e) {
            log.error("Error creating session: {}", e.getMessage());
        }
    }
    
    @MessageMapping("/session.assign")
    public void assignSession(@Payload Map<String, Object> payload) {
        try {
            String sessionId = payload.get("sessionId").toString();
            Long agentId = Long.valueOf(payload.get("agentId").toString());
            String agentName = payload.get("agentName").toString();
            
            ChatSession session = chatService.assignSupportAgent(sessionId, agentId, agentName);
            chatService.notifySessionAssigned(session);
        } catch (Exception e) {
            log.error("Error assigning session: {}", e.getMessage());
        }
    }
    
    // REST API Endpoints
    @RestController
    @RequestMapping("/api/chat")
    @RequiredArgsConstructor
    public static class ChatRestController {
        
        private final ChatService chatService;
        
        @GetMapping("/rooms")
        public ResponseEntity<List<ChatRoom>> getChatRooms() {
            return ResponseEntity.ok(chatService.getActiveChatRooms());
        }
        
        @GetMapping("/rooms/{id}/messages")
        public ResponseEntity<List<ChatMessage>> getRoomMessages(@PathVariable Long id) {
            return ResponseEntity.ok(chatService.getMessagesByRoom(id));
        }
        
        @PostMapping("/rooms/{id}/messages")
        public ResponseEntity<ChatMessage> sendMessage(
                @PathVariable Long id,
                @RequestBody Map<String, Object> messageData) {
            
            String content = messageData.get("content").toString();
            Long senderId = Long.valueOf(messageData.get("senderId").toString());
            String senderName = messageData.get("senderName").toString();
            String senderType = messageData.get("senderType").toString();
            
            ChatMessage message = chatService.sendMessage(
                id, content, senderId, senderName, 
                ChatMessage.SenderType.valueOf(senderType)
            );
            
            return ResponseEntity.ok(message);
        }
        
        @GetMapping("/sessions/waiting")
        public ResponseEntity<List<ChatSession>> getWaitingSessions() {
            return ResponseEntity.ok(chatService.getWaitingSessions());
        }
        
        @GetMapping("/sessions/agent/{agentId}")
        public ResponseEntity<List<ChatSession>> getAgentSessions(@PathVariable Long agentId) {
            return ResponseEntity.ok(chatService.getActiveSessionsByAgent(agentId));
        }
        
        @PostMapping("/sessions/{sessionId}/assign")
        public ResponseEntity<ChatSession> assignSession(
                @PathVariable String sessionId,
                @RequestBody Map<String, Object> assignmentData) {
            
            Long agentId = Long.valueOf(assignmentData.get("agentId").toString());
            String agentName = assignmentData.get("agentName").toString();
            
            ChatSession session = chatService.assignSupportAgent(sessionId, agentId, agentName);
            return ResponseEntity.ok(session);
        }
        
        @PostMapping("/sessions/{sessionId}/end")
        public ResponseEntity<Void> endSession(@PathVariable String sessionId) {
            chatService.endSession(sessionId);
            return ResponseEntity.ok().build();
        }
        
        @PostMapping("/rooms/{id}/mark-read")
        public ResponseEntity<Void> markMessagesAsRead(@PathVariable Long id, @RequestParam Long userId) {
            chatService.markMessagesAsRead(id, userId);
            return ResponseEntity.ok().build();
        }
    }
}
