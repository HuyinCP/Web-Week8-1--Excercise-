<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chat Hỗ Trợ Khách Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .chat-container {
            height: 80vh;
            border: 1px solid #ddd;
            border-radius: 10px;
            overflow: hidden;
        }
        
        .chat-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px;
            font-weight: bold;
        }
        
        .chat-messages {
            height: 60vh;
            overflow-y: auto;
            padding: 20px;
            background-color: #f8f9fa;
        }
        
        .message {
            margin-bottom: 15px;
            display: flex;
            align-items: flex-start;
        }
        
        .message.sent {
            justify-content: flex-end;
        }
        
        .message.received {
            justify-content: flex-start;
        }
        
        .message-content {
            max-width: 70%;
            padding: 10px 15px;
            border-radius: 18px;
            word-wrap: break-word;
        }
        
        .message.sent .message-content {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .message.received .message-content {
            background: white;
            border: 1px solid #e9ecef;
            color: #333;
        }
        
        .message-time {
            font-size: 0.75rem;
            color: #6c757d;
            margin-top: 5px;
        }
        
        .chat-input {
            padding: 20px;
            background: white;
            border-top: 1px solid #e9ecef;
        }
        
        .typing-indicator {
            display: none;
            padding: 10px 15px;
            color: #6c757d;
            font-style: italic;
        }
        
        .online-indicator {
            width: 10px;
            height: 10px;
            background-color: #28a745;
            border-radius: 50%;
            display: inline-block;
            margin-right: 5px;
        }
        
        .support-panel {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 20px;
        }
        
        .session-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 10px;
            background: white;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        
        .session-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        
        .session-card.active {
            border-color: #667eea;
            background: #f0f4ff;
        }
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 0.75rem;
            font-weight: bold;
        }
        
        .status-waiting {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-active {
            background: #d4edda;
            color: #155724;
        }
        
        .status-ended {
            background: #f8d7da;
            color: #721c24;
        }
    </style>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <!-- Customer Chat Panel -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-comments"></i> Chat Khách Hàng</h4>
                    </div>
                    <div class="card-body">
                        <div id="customerChat" class="chat-container">
                            <div class="chat-header">
                                <span class="online-indicator"></span>
                                Hỗ trợ khách hàng
                            </div>
                            <div id="customerMessages" class="chat-messages">
                                <div class="text-center text-muted">
                                    <i class="fas fa-comment-dots fa-3x mb-3"></i>
                                    <p>Chào mừng bạn đến với dịch vụ hỗ trợ khách hàng!</p>
                                    <p>Hãy gửi tin nhắn để bắt đầu cuộc trò chuyện.</p>
                                </div>
                            </div>
                            <div class="typing-indicator" id="customerTyping">
                                <i class="fas fa-circle"></i>
                                <i class="fas fa-circle"></i>
                                <i class="fas fa-circle"></i>
                                Đang nhập...
                            </div>
                            <div class="chat-input">
                                <div class="input-group">
                                    <input type="text" id="customerMessageInput" class="form-control" 
                                           placeholder="Nhập tin nhắn của bạn..." maxlength="500">
                                    <button class="btn btn-primary" id="customerSendBtn">
                                        <i class="fas fa-paper-plane"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Support Agent Panel -->
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h4><i class="fas fa-headset"></i> Bảng Điều Khiển Hỗ Trợ</h4>
                    </div>
                    <div class="card-body">
                        <!-- Session Management -->
                        <div class="support-panel">
                            <h5><i class="fas fa-users"></i> Phiên Chờ</h5>
                            <div id="waitingSessions">
                                <div class="text-center text-muted">
                                    <i class="fas fa-clock fa-2x mb-2"></i>
                                    <p>Không có phiên chờ nào</p>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Active Sessions -->
                        <div class="support-panel">
                            <h5><i class="fas fa-comments"></i> Phiên Đang Hoạt Động</h5>
                            <div id="activeSessions">
                                <div class="text-center text-muted">
                                    <i class="fas fa-comment-slash fa-2x mb-2"></i>
                                    <p>Không có phiên đang hoạt động</p>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Chat Interface for Support -->
                        <div id="supportChat" class="chat-container" style="display: none;">
                            <div class="chat-header">
                                <span class="online-indicator"></span>
                                <span id="supportChatTitle">Chọn phiên để bắt đầu chat</span>
                            </div>
                            <div id="supportMessages" class="chat-messages">
                                <div class="text-center text-muted">
                                    <i class="fas fa-comment-dots fa-3x mb-3"></i>
                                    <p>Chọn một phiên chat để bắt đầu hỗ trợ</p>
                                </div>
                            </div>
                            <div class="typing-indicator" id="supportTyping">
                                <i class="fas fa-circle"></i>
                                <i class="fas fa-circle"></i>
                                <i class="fas fa-circle"></i>
                                Đang nhập...
                            </div>
                            <div class="chat-input">
                                <div class="input-group">
                                    <input type="text" id="supportMessageInput" class="form-control" 
                                           placeholder="Nhập tin nhắn hỗ trợ..." maxlength="500" disabled>
                                    <button class="btn btn-success" id="supportSendBtn" disabled>
                                        <i class="fas fa-paper-plane"></i>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
    <script src="js/chat-client.js"></script>
</body>
</html>
