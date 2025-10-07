class ChatClient {
    constructor() {
        this.stompClient = null;
        this.connected = false;
        this.currentSession = null;
        this.currentChatRoom = null;
        this.customerId = Math.floor(Math.random() * 1000) + 1; // Mock customer ID
        this.customerName = 'Khách hàng ' + this.customerId;
        this.agentId = 1; // Mock agent ID
        this.agentName = 'Nhân viên hỗ trợ';
        
        this.initializeElements();
        this.connectWebSocket();
        this.setupEventListeners();
        this.loadInitialData();
    }
    
    initializeElements() {
        // Customer elements
        this.customerMessagesDiv = document.getElementById('customerMessages');
        this.customerMessageInput = document.getElementById('customerMessageInput');
        this.customerSendBtn = document.getElementById('customerSendBtn');
        this.customerTyping = document.getElementById('customerTyping');
        
        // Support elements
        this.supportMessagesDiv = document.getElementById('supportMessages');
        this.supportMessageInput = document.getElementById('supportMessageInput');
        this.supportSendBtn = document.getElementById('supportSendBtn');
        this.supportTyping = document.getElementById('supportTyping');
        this.supportChat = document.getElementById('supportChat');
        this.supportChatTitle = document.getElementById('supportChatTitle');
        this.waitingSessionsDiv = document.getElementById('waitingSessions');
        this.activeSessionsDiv = document.getElementById('activeSessions');
    }
    
    connectWebSocket() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.connect({}, (frame) => {
            console.log('Connected: ' + frame);
            this.connected = true;
            
            // Subscribe to public messages
            this.stompClient.subscribe('/topic/public', (message) => {
                this.handlePublicMessage(JSON.parse(message.body));
            });
            
            // Subscribe to support notifications
            this.stompClient.subscribe('/topic/support/notifications', (message) => {
                this.handleNewSessionNotification(JSON.parse(message.body));
            });
            
            // Subscribe to session updates
            this.stompClient.subscribe('/topic/session/+', (message) => {
                this.handleSessionUpdate(JSON.parse(message.body));
            });
            
            // Subscribe to room messages
            this.stompClient.subscribe('/topic/room/+', (message) => {
                this.handleRoomMessage(JSON.parse(message.body));
            });
            
        }, (error) => {
            console.log('STOMP error: ' + error);
            this.connected = false;
            setTimeout(() => this.connectWebSocket(), 5000);
        });
    }
    
    setupEventListeners() {
        // Customer message sending
        this.customerSendBtn.addEventListener('click', () => {
            this.sendCustomerMessage();
        });
        
        this.customerMessageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendCustomerMessage();
            }
        });
        
        // Support message sending
        this.supportSendBtn.addEventListener('click', () => {
            this.sendSupportMessage();
        });
        
        this.supportMessageInput.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendSupportMessage();
            }
        });
        
        // Typing indicators
        this.customerMessageInput.addEventListener('input', () => {
            this.sendTypingIndicator('customer');
        });
        
        this.supportMessageInput.addEventListener('input', () => {
            this.sendTypingIndicator('support');
        });
    }
    
    loadInitialData() {
        this.loadWaitingSessions();
        this.loadActiveSessions();
    }
    
    async loadWaitingSessions() {
        try {
            const response = await fetch('/api/chat/sessions/waiting');
            const sessions = await response.json();
            this.displayWaitingSessions(sessions);
        } catch (error) {
            console.error('Error loading waiting sessions:', error);
        }
    }
    
    async loadActiveSessions() {
        try {
            const response = await fetch(`/api/chat/sessions/agent/${this.agentId}`);
            const sessions = await response.json();
            this.displayActiveSessions(sessions);
        } catch (error) {
            console.error('Error loading active sessions:', error);
        }
    }
    
    displayWaitingSessions(sessions) {
        if (sessions.length === 0) {
            this.waitingSessionsDiv.innerHTML = `
                <div class="text-center text-muted">
                    <i class="fas fa-clock fa-2x mb-2"></i>
                    <p>Không có phiên chờ nào</p>
                </div>
            `;
            return;
        }
        
        this.waitingSessionsDiv.innerHTML = sessions.map(session => `
            <div class="session-card" onclick="chatClient.assignSession('${session.sessionId}')">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${session.customerName}</strong>
                        <br>
                        <small class="text-muted">ID: ${session.customerId}</small>
                    </div>
                    <span class="status-badge status-waiting">Chờ</span>
                </div>
                <small class="text-muted">
                    <i class="fas fa-clock"></i> ${this.formatDateTime(session.createdAt)}
                </small>
            </div>
        `).join('');
    }
    
    displayActiveSessions(sessions) {
        if (sessions.length === 0) {
            this.activeSessionsDiv.innerHTML = `
                <div class="text-center text-muted">
                    <i class="fas fa-comment-slash fa-2x mb-2"></i>
                    <p>Không có phiên đang hoạt động</p>
                </div>
            `;
            return;
        }
        
        this.activeSessionsDiv.innerHTML = sessions.map(session => `
            <div class="session-card ${session.sessionId === this.currentSession?.sessionId ? 'active' : ''}" 
                 onclick="chatClient.selectSession('${session.sessionId}')">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${session.customerName}</strong>
                        <br>
                        <small class="text-muted">ID: ${session.customerId}</small>
                    </div>
                    <span class="status-badge status-active">Hoạt động</span>
                </div>
                <small class="text-muted">
                    <i class="fas fa-user-tie"></i> ${session.supportAgentName || 'Chưa gán'}
                </small>
            </div>
        `).join('');
    }
    
    sendCustomerMessage() {
        const content = this.customerMessageInput.value.trim();
        if (!content || !this.connected) return;
        
        // Create session if not exists
        if (!this.currentSession) {
            this.createCustomerSession();
        }
        
        // Send message
        const message = {
            chatRoomId: this.currentChatRoom?.id || 1,
            content: content,
            senderId: this.customerId,
            senderName: this.customerName,
            senderType: 'CUSTOMER'
        };
        
        this.stompClient.send('/app/room.sendMessage', {}, JSON.stringify(message));
        this.customerMessageInput.value = '';
    }
    
    sendSupportMessage() {
        const content = this.supportMessageInput.value.trim();
        if (!content || !this.connected || !this.currentSession) return;
        
        const message = {
            chatRoomId: this.currentChatRoom?.id || 1,
            content: content,
            senderId: this.agentId,
            senderName: this.agentName,
            senderType: 'SUPPORT_AGENT'
        };
        
        this.stompClient.send('/app/room.sendMessage', {}, JSON.stringify(message));
        this.supportMessageInput.value = '';
    }
    
    createCustomerSession() {
        const sessionData = {
            customerId: this.customerId,
            customerName: this.customerName
        };
        
        this.stompClient.send('/app/session.create', {}, JSON.stringify(sessionData));
    }
    
    assignSession(sessionId) {
        const assignmentData = {
            sessionId: sessionId,
            agentId: this.agentId,
            agentName: this.agentName
        };
        
        this.stompClient.send('/app/session.assign', {}, JSON.stringify(assignmentData));
    }
    
    selectSession(sessionId) {
        // Find session in active sessions
        const sessionCards = document.querySelectorAll('#activeSessions .session-card');
        sessionCards.forEach(card => card.classList.remove('active'));
        
        // Load session messages
        this.loadSessionMessages(sessionId);
        
        // Enable support chat
        this.supportChat.style.display = 'block';
        this.supportMessageInput.disabled = false;
        this.supportSendBtn.disabled = false;
    }
    
    async loadSessionMessages(sessionId) {
        try {
            // This would typically load messages for the specific session
            // For now, we'll use a mock chat room ID
            const response = await fetch('/api/chat/rooms/1/messages');
            const messages = await response.json();
            this.displaySupportMessages(messages);
        } catch (error) {
            console.error('Error loading session messages:', error);
        }
    }
    
    displayCustomerMessages(messages) {
        if (messages.length === 0) {
            this.customerMessagesDiv.innerHTML = `
                <div class="text-center text-muted">
                    <i class="fas fa-comment-dots fa-3x mb-3"></i>
                    <p>Chào mừng bạn đến với dịch vụ hỗ trợ khách hàng!</p>
                    <p>Hãy gửi tin nhắn để bắt đầu cuộc trò chuyện.</p>
                </div>
            `;
            return;
        }
        
        this.customerMessagesDiv.innerHTML = messages.map(message => `
            <div class="message ${message.senderType === 'CUSTOMER' ? 'sent' : 'received'}">
                <div class="message-content">
                    <div>${this.escapeHtml(message.content)}</div>
                    <div class="message-time">
                        ${this.formatDateTime(message.createdAt)} - ${message.senderName}
                    </div>
                </div>
            </div>
        `).join('');
        
        this.scrollToBottom(this.customerMessagesDiv);
    }
    
    displaySupportMessages(messages) {
        if (messages.length === 0) {
            this.supportMessagesDiv.innerHTML = `
                <div class="text-center text-muted">
                    <i class="fas fa-comment-dots fa-3x mb-3"></i>
                    <p>Chọn một phiên chat để bắt đầu hỗ trợ</p>
                </div>
            `;
            return;
        }
        
        this.supportMessagesDiv.innerHTML = messages.map(message => `
            <div class="message ${message.senderType === 'SUPPORT_AGENT' ? 'sent' : 'received'}">
                <div class="message-content">
                    <div>${this.escapeHtml(message.content)}</div>
                    <div class="message-time">
                        ${this.formatDateTime(message.createdAt)} - ${message.senderName}
                    </div>
                </div>
            </div>
        `).join('');
        
        this.scrollToBottom(this.supportMessagesDiv);
    }
    
    handlePublicMessage(message) {
        console.log('Public message received:', message);
    }
    
    handleNewSessionNotification(session) {
        console.log('New session notification:', session);
        this.loadWaitingSessions();
    }
    
    handleSessionUpdate(session) {
        console.log('Session update:', session);
        this.loadWaitingSessions();
        this.loadActiveSessions();
    }
    
    handleRoomMessage(message) {
        console.log('Room message received:', message);
        
        // Display message in appropriate chat
        if (message.senderType === 'CUSTOMER') {
            this.displayCustomerMessages([message]);
        } else if (message.senderType === 'SUPPORT_AGENT') {
            this.displaySupportMessages([message]);
        }
    }
    
    sendTypingIndicator(type) {
        // Implement typing indicator logic
        const typingDiv = type === 'customer' ? this.customerTyping : this.supportTyping;
        typingDiv.style.display = 'block';
        
        setTimeout(() => {
            typingDiv.style.display = 'none';
        }, 2000);
    }
    
    scrollToBottom(element) {
        element.scrollTop = element.scrollHeight;
    }
    
    formatDateTime(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString('vi-VN');
    }
    
    escapeHtml(text) {
        const div = document.createElement('div');
        div.textContent = text;
        return div.innerHTML;
    }
}

// Initialize chat client when page loads
let chatClient;
document.addEventListener('DOMContentLoaded', () => {
    chatClient = new ChatClient();
});
