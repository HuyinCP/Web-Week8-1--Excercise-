# Ứng Dụng GraphQL Spring Boot 3 với Hệ Thống Chat WebSocket

Đây là ứng dụng Spring Boot 3 hoàn chỉnh với GraphQL API để quản lý sản phẩm, người dùng, danh mục và hệ thống chat hỗ trợ khách hàng real-time sử dụng WebSocket.

## 🚀 Tính Năng Chính

### 📊 GraphQL API
- **Quản lý sản phẩm**: CRUD operations với sắp xếp theo giá
- **Quản lý người dùng**: CRUD operations với mối quan hệ nhiều-nhiều
- **Quản lý danh mục**: CRUD operations với liên kết người dùng
- **Truy vấn sản phẩm theo danh mục**

### 💬 Hệ Thống Chat WebSocket
- **Chat real-time** giữa khách hàng và nhân viên hỗ trợ
- **Quản lý phiên chat** (tạo, gán, kết thúc)
- **Thông báo real-time** cho nhân viên hỗ trợ
- **Giao diện responsive** với Bootstrap 5
- **Trạng thái đang nhập** và thông báo tin nhắn mới

## 🏗️ Cấu Trúc Database

### GraphQL Models
- **Category**: id, name, images
- **User**: id, fullname, email, password, phone
- **Product**: id, title, quantity, description, price, user_id
- **User-Category**: Mối quan hệ nhiều-nhiều

### Chat Models
- **ChatRoom**: id, name, roomType, createdAt, isActive
- **ChatMessage**: id, content, messageType, senderId, senderName, senderType, createdAt, isRead
- **ChatSession**: id, sessionId, customerId, customerName, supportAgentId, supportAgentName, status, createdAt, endedAt

## 🛠️ Công Nghệ Sử Dụng

### Backend
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring GraphQL**
- **Spring WebSocket** với STOMP protocol
- **H2 Database** (in-memory)
- **Lombok** để giảm boilerplate code

### Frontend
- **JSP/JSTL** cho GraphQL interface
- **Bootstrap 5** cho UI styling
- **SockJS + STOMP.js** cho WebSocket
- **JavaScript ES6+** cho client-side logic
- **Font Awesome** cho icons

## 📁 Cấu Trúc Dự Án

```
src/
├── main/
│   ├── java/
│   │   └── com/example/graphql/
│   │       ├── GraphqlApplication.java
│   │       ├── config/
│   │       │   ├── DataInitializer.java
│   │       │   └── WebSocketConfig.java
│   │       ├── controller/
│   │       │   ├── ViewController.java
│   │       │   └── ChatController.java
│   │       ├── model/
│   │       │   ├── Category.java
│   │       │   ├── Product.java
│   │       │   ├── User.java
│   │       │   ├── ChatRoom.java
│   │       │   ├── ChatMessage.java
│   │       │   └── ChatSession.java
│   │       ├── repository/
│   │       │   ├── CategoryRepository.java
│   │       │   ├── ProductRepository.java
│   │       │   ├── UserRepository.java
│   │       │   ├── ChatRoomRepository.java
│   │       │   ├── ChatMessageRepository.java
│   │       │   └── ChatSessionRepository.java
│   │       ├── resolver/
│   │       │   ├── CategoryResolver.java
│   │       │   ├── ProductResolver.java
│   │       │   └── UserResolver.java
│   │       └── service/
│   │           └── ChatService.java
│   ├── resources/
│   │   ├── application.yml
│   │   └── graphql/
│   │       └── schema.graphqls
│   └── webapp/
│       ├── WEB-INF/
│       │   └── jsp/
│       │       ├── index.jsp
│       │       └── chat.jsp
│       └── js/
│           ├── graphql-client.js
│           └── chat-client.js
└── pom.xml
```

## 🚀 Hướng Dẫn Cài Đặt và Chạy

### Yêu Cầu Hệ Thống
- Java 17 trở lên
- Maven 3.6 trở lên

### Các Bước Cài Đặt

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
- **Trang chủ GraphQL**: http://localhost:8080
- **Chat Hỗ Trợ**: http://localhost:8080/chat
- **GraphQL Playground**: http://localhost:8080/graphiql
- **H2 Database Console**: http://localhost:8080/h2-console

### Kết Nối H2 Database
- **URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

## 📊 GraphQL API Endpoints

### Truy Vấn (Queries)

#### Lấy tất cả sản phẩm (sắp xếp theo giá từ thấp đến cao)
```graphql
query {
  allProducts {
    id
    title
    quantity
    description
    price
    user {
      id
      fullname
      email
    }
  }
}
```

#### Lấy sản phẩm theo danh mục
```graphql
query {
  productsByCategory(categoryId: "1") {
    id
    title
    quantity
    description
    price
    user {
      id
      fullname
    }
  }
}
```

#### Lấy tất cả người dùng
```graphql
query {
  allUsers {
    id
    fullname
    email
    phone
    categories {
      id
      name
    }
  }
}
```

### Đột Biến (Mutations)

#### Tạo sản phẩm mới
```graphql
mutation {
  createProduct(input: {
    title: "Sản phẩm mới"
    quantity: 10
    description: "Mô tả sản phẩm"
    price: 99.99
    userId: "1"
  }) {
    id
    title
    price
  }
}
```

#### Tạo người dùng mới
```graphql
mutation {
  createUser(input: {
    fullname: "Nguyễn Văn A"
    email: "nguyenvana@example.com"
    password: "password123"
    phone: "0123-456-789"
  }) {
    id
    fullname
    email
  }
}
```

## 💬 Chat API Endpoints

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

## 🎯 Cách Sử Dụng Chat System

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

## ✅ Tính Năng Đã Triển Khai

### GraphQL System
- ✅ **GraphQL API với Spring Boot 3**
- ✅ **Sản phẩm sắp xếp theo giá từ thấp đến cao**
- ✅ **Truy vấn sản phẩm theo danh mục**
- ✅ **Đầy đủ thao tác CRUD cho User, Product, và Category**
- ✅ **Mối quan hệ nhiều-nhiều giữa User và Category**
- ✅ **Frontend JSP với tích hợp AJAX**
- ✅ **UI Bootstrap để trải nghiệm người dùng tốt hơn**
- ✅ **Khởi tạo dữ liệu mẫu**
- ✅ **GraphQL Playground để test**

### Chat System
- ✅ **Chat real-time với WebSocket**
- ✅ **Quản lý phiên chat (tạo, gán, kết thúc)**
- ✅ **Thông báo real-time cho nhân viên hỗ trợ**
- ✅ **Giao diện responsive với Bootstrap 5**
- ✅ **Trạng thái đang nhập**
- ✅ **REST API cho chat management**
- ✅ **WebSocket với STOMP protocol**
- ✅ **SockJS fallback support**

## 📊 Dữ Liệu Mẫu

Ứng dụng tự động khởi tạo với dữ liệu mẫu bao gồm:

### GraphQL Data
- **3 Danh mục**: Điện tử, Quần áo, Sách
- **3 Người dùng** với các liên kết danh mục khác nhau
- **6 Sản phẩm** với giá khác nhau để test chức năng sắp xếp

### Chat Data
- **2 Phòng chat**: Customer Support, General Chat
- **Sẵn sàng** cho việc tạo phiên chat mới

## 🔧 Cấu Hình

### application.yml
```yaml
server:
  port: 8080

spring:
  application:
    name: graphql-spring-boot
  
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
    username: sa
    password: password
  
  h2:
    console:
      enabled: true
      path: '/h2-console'
  
  jpa:
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  
  graphql:
    graphiql:
      enabled: true
      path: '/graphiql'
    path: '/graphql'
  
  websocket:
    servlet:
      allowed-origins: "*"

logging:
  level:
    com.example.graphql: DEBUG
    org.springframework.graphql: DEBUG
    org.springframework.messaging: DEBUG
    org.springframework.web.socket: DEBUG
```

## 🧪 Cách Test

### GraphQL API
1. Sử dụng giao diện web tại http://localhost:8080
2. Sử dụng GraphQL Playground tại http://localhost:8080/graphiql
3. Test chức năng AJAX thông qua giao diện web
4. Xác minh mối quan hệ nhiều-nhiều giữa users và categories

### Chat System
1. Mở 2 tab browser: http://localhost:8080/chat
2. Tab 1: Giả lập khách hàng (gửi tin nhắn bên trái)
3. Tab 2: Giả lập nhân viên hỗ trợ (gán phiên và chat bên phải)
4. Test real-time messaging và notifications

## 🔮 Tính Năng Có Thể Mở Rộng

### GraphQL
- 🔄 Pagination cho queries
- 🔄 Caching với Redis
- 🔄 Authentication & Authorization
- 🔄 File upload support
- 🔄 Subscription cho real-time updates

### Chat System
- 🔄 Lưu trữ lịch sử chat
- 🔄 Gửi file/ảnh
- 🔄 Emoji reactions
- 🔄 Chat bot tự động
- 🔄 Phân tích tâm trạng
- 🔄 Báo cáo thống kê
- 🔄 Multi-language support
- 🔄 Voice messages
- 🔄 Screen sharing

## 🛡️ Bảo Mật

### Hiện Tại
- CORS enabled cho development
- Basic input validation

### Khuyến Nghị
- Implement JWT authentication
- Add rate limiting
- Encrypt sensitive data
- Validate all inputs
- Use HTTPS in production
- Add CSRF protection

## 📝 License

MIT License - Xem file LICENSE để biết thêm chi tiết.

## 🤝 Đóng Góp

1. Fork dự án
2. Tạo feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Create Pull Request

## 📞 Liên Hệ

Nếu có câu hỏi hoặc góp ý, vui lòng tạo issue trên GitHub repository.

---

**Lưu ý**: Đây là dự án demo cho mục đích học tập. Trong môi trường production, cần thêm các biện pháp bảo mật và tối ưu hóa phù hợp.