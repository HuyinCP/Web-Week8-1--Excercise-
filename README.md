# Ứng dụng GraphQL Spring Boot 3

Đây là ứng dụng Spring Boot 3 với GraphQL API để quản lý sản phẩm, người dùng và danh mục với mối quan hệ nhiều-nhiều.

## Cấu trúc Database

- **Category**: id, name, images
- **User**: id, fullname, email, password, phone
- **Product**: id, title, quantity, description, price, user_id
- **User-Category**: Mối quan hệ nhiều-nhiều

## Tính năng

### GraphQL Queries
1. **Hiển thị sản phẩm theo giá từ thấp đến cao**
2. **Lấy sản phẩm theo danh mục**
3. **Thao tác CRUD cho User, Product, và Category**

### Frontend
- Giao diện JSP với tích hợp AJAX
- UI Bootstrap để trải nghiệm người dùng tốt hơn
- Truy vấn và đột biến GraphQL thời gian thực

## Hướng dẫn sử dụng

### Yêu cầu hệ thống
- Java 17 trở lên
- Maven 3.6 trở lên

### Chạy ứng dụng

1. **Tải xuống dự án**

2. **Di chuyển đến thư mục dự án**
   ```bash
   cd Web-Week8-1--Excercise-
   ```

3. **Chạy ứng dụng**
   ```bash
   mvn spring-boot:run
   ```

4. **Truy cập ứng dụng**
   - Giao diện chính: http://localhost:8080
   - GraphQL Playground: http://localhost:8080/graphiql
   - H2 Database Console: http://localhost:8080/h2-console

### Kết nối H2 Database
- URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## GraphQL API Endpoints

### Truy vấn (Queries)

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

#### Lấy tất cả danh mục
```graphql
query {
  allCategories {
    id
    name
    images
    users {
      id
      fullname
      email
    }
  }
}
```

### Đột biến (Mutations)

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

#### Tạo danh mục mới
```graphql
mutation {
  createCategory(input: {
    name: "Điện tử"
    images: "electronics.jpg"
  }) {
    id
    name
  }
}
```

## Cấu trúc dự án

```
src/
├── main/
│   ├── java/
│   │   └── com/example/graphql/
│   │       ├── GraphqlApplication.java
│   │       ├── config/
│   │       │   └── DataInitializer.java
│   │       ├── controller/
│   │       │   └── ViewController.java
│   │       ├── model/
│   │       │   ├── Category.java
│   │       │   ├── Product.java
│   │       │   └── User.java
│   │       ├── repository/
│   │       │   ├── CategoryRepository.java
│   │       │   ├── ProductRepository.java
│   │       │   └── UserRepository.java
│   │       └── resolver/
│   │           ├── CategoryResolver.java
│   │           ├── ProductResolver.java
│   │           └── UserResolver.java
│   ├── resources/
│   │   ├── application.yml
│   │   └── graphql/
│   │       └── schema.graphqls
│   └── webapp/
│       ├── WEB-INF/
│       │   └── jsp/
│       │       └── index.jsp
│       └── js/
│           └── graphql-client.js
└── pom.xml
```

## Công nghệ sử dụng

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring GraphQL**
- **H2 Database** (in-memory)
- **JSP/JSTL** cho frontend
- **Bootstrap 5** cho UI styling
- **Lombok** để giảm boilerplate code

## Tính năng đã triển khai

✅ **GraphQL API với Spring Boot 3**
✅ **Sản phẩm sắp xếp theo giá từ thấp đến cao**
✅ **Truy vấn sản phẩm theo danh mục**
✅ **Đầy đủ thao tác CRUD cho User, Product, và Category**
✅ **Mối quan hệ nhiều-nhiều giữa User và Category**
✅ **Frontend JSP với tích hợp AJAX**
✅ **UI Bootstrap để trải nghiệm người dùng tốt hơn**
✅ **Khởi tạo dữ liệu mẫu**
✅ **GraphQL Playground để test**

## Dữ liệu mẫu

Ứng dụng tự động khởi tạo với dữ liệu mẫu bao gồm:
- 3 Danh mục (Điện tử, Quần áo, Sách)
- 3 Người dùng với các liên kết danh mục khác nhau
- 6 Sản phẩm với giá khác nhau để test chức năng sắp xếp

## Cách test API

1. Sử dụng giao diện web tại http://localhost:8080
2. Sử dụng GraphQL Playground tại http://localhost:8080/graphiql
3. Test chức năng AJAX thông qua giao diện web
4. Xác minh mối quan hệ nhiều-nhiều giữa users và categories
