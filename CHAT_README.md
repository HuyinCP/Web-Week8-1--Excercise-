# Hệ Thống Chat Hỗ Trợ Khách Hàng với WebSocket và Spring Boot

## Tổng Quan

Hệ thống chat hỗ trợ khách hàng được xây dựng sử dụng WebSocket và Spring Boot, cho phép khách hàng và nhân viên hỗ trợ giao tiếp real-time.

## Tính Năng Chính

### 1. Chat Real-time
- Gửi và nhận tin nhắn tức thời
- Hiển thị trạng thái đang nhập
- Thông báo tin nhắn mới

### 2. Quản Lý Phiên Chat
- Tạo phiên chat mới cho khách hàng
- Gán nhân viên hỗ trợ cho phiên
- Theo dõi trạng thái phiên (Chờ, Hoạt động, Kết thúc)

### 3. Giao Diện Người Dùng
- Giao diện chat cho khách hàng
- Bảng điều khiển cho nhân viên hỗ trợ
- Thiết kế responsive và thân thiện

## Cấu Trúc Dự Án

### Backend (Spring Boot)

#### Models
- `ChatRoom`: Quản lý phòng chat
- `ChatMessage`: Lưu trữ tin nhắn
- `ChatSession`: Quản lý phiên chat

#### Controllers
- `ChatController`: Xử lý WebSocket và REST API
- `ViewController`: Điều hướng trang web

#### Services
- `ChatService`: Logic nghiệp vụ chat

#### Configuration
- `WebSocketConfig`: Cấu hình WebSocket
- `DataInitializer`: Khởi tạo dữ liệu mẫu

### Frontend
- `chat.jsp`: Giao diện chat chính
- `chat-client.js`: JavaScript client cho WebSocket

## Cài Đặt và Chạy

### Yêu Cầu
- Java 17+
- Maven 3.6+
- Spring Boot 3.2.0

### Các Bước

1. **Clone dự án**
```bash
git clone <repository-url>
cd Web-Week8-1--Excercise-
```

2. **Cài đặt dependencies**
```bash
mvn clean install
```

3. **Chạy ứng dụng**
```bash
mvn spring-boot:run
```

4. **Truy cập ứng dụng**
- Trang chủ: http://localhost:8080
- Chat: http://localhost:8080/chat
- GraphQL Playground: http://localhost:8080/graphiql
- H2 Console: http://localhost:8080/h2-console

## Sử Dụng

### Cho Khách Hàng
1. Truy cập http://localhost:8080/chat
2. Nhập tin nhắn vào ô chat bên trái
3. Nhấn Enter hoặc nút gửi
4. Chờ nhân viên hỗ trợ phản hồi

### Cho Nhân Viên Hỗ Trợ
1. Truy cập http://localhost:8080/chat
2. Xem danh sách phiên chờ ở bảng điều khiển bên phải
3. Click vào phiên chờ để gán cho mình
4. Bắt đầu chat với khách hàng

## API Endpoints

### REST API
- `GET /api/chat/rooms` - Lấy danh sách phòng chat
- `GET /api/chat/rooms/{id}/messages` - Lấy tin nhắn của phòng
- `POST /api/chat/rooms/{id}/messages` - Gửi tin nhắn
- `GET /api/chat/sessions/waiting` - Lấy phiên chờ
- `GET /api/chat/sessions/agent/{agentId}` - Lấy phiên của nhân viên
- `POST /api/chat/sessions/{sessionId}/assign` - Gán phiên
- `POST /api/chat/sessions/{sessionId}/end` - Kết thúc phiên

### WebSocket Endpoints
- `/ws` - Kết nối WebSocket
- `/app/room.sendMessage` - Gửi tin nhắn đến phòng
- `/app/session.create` - Tạo phiên mới
- `/app/session.assign` - Gán phiên
- `/topic/room/{roomId}` - Nhận tin nhắn từ phòng
- `/topic/support/notifications` - Thông báo cho nhân viên hỗ trợ

## Cấu Hình

### application.yml
```yaml
server:
  port: 8080

spring:
  websocket:
    servlet:
      allowed-origins: "*"
  
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
```

## Công Nghệ Sử Dụng

### Backend
- Spring Boot 3.2.0
- Spring WebSocket
- Spring Data JPA
- H2 Database
- STOMP Protocol
- Lombok

### Frontend
- HTML5/CSS3
- JavaScript (ES6+)
- Bootstrap 5
- SockJS
- STOMP.js
- Font Awesome

## Tính Năng Nâng Cao

### Đã Triển Khai
- ✅ Chat real-time
- ✅ Quản lý phiên chat
- ✅ Giao diện responsive
- ✅ Thông báo real-time
- ✅ Trạng thái đang nhập

### Có Thể Mở Rộng
- 🔄 Lưu trữ lịch sử chat
- 🔄 Gửi file/ảnh
- 🔄 Emoji reactions
- 🔄 Chat bot tự động
- 🔄 Phân tích tâm trạng
- 🔄 Báo cáo thống kê
- 🔄 Multi-language support

## Xử Lý Lỗi

### Lỗi Thường Gặp
1. **WebSocket connection failed**
   - Kiểm tra firewall
   - Đảm bảo port 8080 không bị chiếm

2. **Database connection error**
   - Kiểm tra H2 database configuration
   - Restart ứng dụng

3. **Messages not sending**
   - Kiểm tra WebSocket connection
   - Xem console logs

## Bảo Mật

### Hiện Tại
- CORS enabled cho development
- Basic input validation

### Khuyến Nghị
- Implement authentication
- Add rate limiting
- Encrypt sensitive data
- Validate all inputs
- Use HTTPS in production

## Đóng Góp

1. Fork dự án
2. Tạo feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

MIT License - Xem file LICENSE để biết thêm chi tiết.

## Liên Hệ

Nếu có câu hỏi hoặc góp ý, vui lòng tạo issue trên GitHub repository.
