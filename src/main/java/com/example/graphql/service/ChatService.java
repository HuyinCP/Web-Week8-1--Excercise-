package com.example.graphql.service;

import com.example.graphql.model.*;
import com.example.graphql.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ChatService {
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    // Chat Room Management
    public ChatRoom createChatRoom(String name, ChatRoom.RoomType roomType) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setName(name);
        chatRoom.setRoomType(roomType);
        chatRoom.setIsActive(true);
        return chatRoomRepository.save(chatRoom);
    }
    
    public List<ChatRoom> getActiveChatRooms() {
        return chatRoomRepository.findByIsActiveTrue();
    }
    
    public Optional<ChatRoom> getChatRoomById(Long id) {
        return chatRoomRepository.findById(id);
    }
    
    // Chat Session Management
    public ChatSession createChatSession(Long customerId, String customerName) {
        ChatSession session = new ChatSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setCustomerId(customerId);
        session.setCustomerName(customerName);
        session.setStatus(ChatSession.SessionStatus.WAITING);
        
        // Create or get customer support room
        ChatRoom supportRoom = getOrCreateSupportRoom();
        session.setChatRoom(supportRoom);
        
        return chatSessionRepository.save(session);
    }
    
    public ChatSession assignSupportAgent(String sessionId, Long agentId, String agentName) {
        Optional<ChatSession> sessionOpt = chatSessionRepository.findBySessionId(sessionId);
        if (sessionOpt.isPresent()) {
            ChatSession session = sessionOpt.get();
            session.setSupportAgentId(agentId);
            session.setSupportAgentName(agentName);
            session.setStatus(ChatSession.SessionStatus.ACTIVE);
            return chatSessionRepository.save(session);
        }
        throw new RuntimeException("Session not found: " + sessionId);
    }
    
    public List<ChatSession> getWaitingSessions() {
        return chatSessionRepository.findWaitingSessions();
    }
    
    public List<ChatSession> getActiveSessionsByAgent(Long agentId) {
        return chatSessionRepository.findActiveSessionsByAgent(agentId);
    }
    
    // Message Management
    public ChatMessage sendMessage(Long chatRoomId, String content, Long senderId, 
                                 String senderName, ChatMessage.SenderType senderType) {
        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(chatRoomId);
        if (roomOpt.isEmpty()) {
            throw new RuntimeException("Chat room not found: " + chatRoomId);
        }
        
        ChatMessage message = new ChatMessage();
        message.setContent(content);
        message.setMessageType(ChatMessage.MessageType.TEXT);
        message.setSenderId(senderId);
        message.setSenderName(senderName);
        message.setSenderType(senderType);
        message.setChatRoom(roomOpt.get());
        message.setIsRead(false);
        
        ChatMessage savedMessage = chatMessageRepository.save(message);
        
        // Send message via WebSocket
        sendMessageToRoom(chatRoomId, savedMessage);
        
        return savedMessage;
    }
    
    public List<ChatMessage> getMessagesByRoom(Long chatRoomId) {
        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(chatRoomId);
        if (roomOpt.isPresent()) {
            return chatMessageRepository.findByChatRoomOrderByCreatedAtAsc(roomOpt.get());
        }
        return List.of();
    }
    
    public void markMessagesAsRead(Long chatRoomId, Long userId) {
        Optional<ChatRoom> roomOpt = chatRoomRepository.findById(chatRoomId);
        if (roomOpt.isPresent()) {
            List<ChatMessage> unreadMessages = chatMessageRepository.findUnreadMessagesByChatRoom(roomOpt.get());
            unreadMessages.forEach(message -> {
                if (!message.getSenderId().equals(userId)) {
                    message.setIsRead(true);
                }
            });
            chatMessageRepository.saveAll(unreadMessages);
        }
    }
    
    // WebSocket Messaging
    private void sendMessageToRoom(Long chatRoomId, ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/room/" + chatRoomId, message);
        log.info("Message sent to room {}: {}", chatRoomId, message.getContent());
    }
    
    public void sendSystemMessage(Long chatRoomId, String content) {
        sendMessage(chatRoomId, content, 0L, "System", ChatMessage.SenderType.SYSTEM);
    }
    
    public void notifyNewSession(ChatSession session) {
        messagingTemplate.convertAndSend("/topic/support/notifications", session);
        log.info("New session notification sent: {}", session.getSessionId());
    }
    
    public void notifySessionAssigned(ChatSession session) {
        messagingTemplate.convertAndSend("/topic/session/" + session.getSessionId(), session);
        log.info("Session assignment notification sent: {}", session.getSessionId());
    }
    
    // Helper Methods
    private ChatRoom getOrCreateSupportRoom() {
        Optional<ChatRoom> supportRoom = chatRoomRepository.findByNameAndIsActiveTrue("Customer Support");
        if (supportRoom.isPresent()) {
            return supportRoom.get();
        }
        return createChatRoom("Customer Support", ChatRoom.RoomType.CUSTOMER_SUPPORT);
    }
    
    public void endSession(String sessionId) {
        Optional<ChatSession> sessionOpt = chatSessionRepository.findBySessionId(sessionId);
        if (sessionOpt.isPresent()) {
            ChatSession session = sessionOpt.get();
            session.setStatus(ChatSession.SessionStatus.ENDED);
            session.setEndedAt(LocalDateTime.now());
            chatSessionRepository.save(session);
            
            // Notify about session end
            messagingTemplate.convertAndSend("/topic/session/" + sessionId, session);
        }
    }
}
