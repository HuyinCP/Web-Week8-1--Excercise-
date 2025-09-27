# GraphQL Spring Boot 3 Application

This is a Spring Boot 3 application with GraphQL API for managing products, users, and categories with many-to-many relationships.

## Database Schema

- **Category**: id, name, images
- **User**: id, fullname, email, password, phone
- **Product**: id, title, quantity, description, price, user_id
- **User-Category**: Many-to-many relationship

## Features

### GraphQL Queries
1. **Products sorted by price (low to high)**
2. **Products by category**
3. **CRUD operations for User, Product, and Category**

### Frontend
- JSP view with AJAX integration
- Bootstrap UI for better user experience
- Real-time GraphQL queries and mutations

## Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Running the Application

1. **Clone or download the project**

2. **Navigate to project directory**
   ```bash
   cd Web-Week8-1--Excercise-
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main UI: http://localhost:8080
   - GraphQL Playground: http://localhost:8080/graphiql
   - H2 Database Console: http://localhost:8080/h2-console

### H2 Database Connection
- URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## GraphQL API Endpoints

### Queries

#### Get All Products (Sorted by Price)
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

#### Get Products by Category
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

#### Get All Users
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

#### Get All Categories
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

### Mutations

#### Create Product
```graphql
mutation {
  createProduct(input: {
    title: "New Product"
    quantity: 10
    description: "Product description"
    price: 99.99
    userId: "1"
  }) {
    id
    title
    price
  }
}
```

#### Create User
```graphql
mutation {
  createUser(input: {
    fullname: "John Doe"
    email: "john@example.com"
    password: "password123"
    phone: "123-456-7890"
  }) {
    id
    fullname
    email
  }
}
```

#### Create Category
```graphql
mutation {
  createCategory(input: {
    name: "Electronics"
    images: "electronics.jpg"
  }) {
    id
    name
  }
}
```

## Project Structure

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

## Technologies Used

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Spring GraphQL**
- **H2 Database** (in-memory)
- **JSP/JSTL** for frontend
- **Bootstrap 5** for UI styling
- **Lombok** for reducing boilerplate code

## Features Implemented

✅ **GraphQL API with Spring Boot 3**
✅ **Products sorted by price (low to high)**
✅ **Products by category query**
✅ **Full CRUD operations for User, Product, and Category**
✅ **Many-to-many relationship between User and Category**
✅ **JSP frontend with AJAX integration**
✅ **Bootstrap UI for better user experience**
✅ **Sample data initialization**
✅ **GraphQL Playground for testing**

## Sample Data

The application automatically initializes with sample data including:
- 3 Categories (Electronics, Clothing, Books)
- 3 Users with different category associations
- 6 Products with various prices for testing sorting functionality

## Testing the API

1. Use the web interface at http://localhost:8080
2. Use GraphQL Playground at http://localhost:8080/graphiql
3. Test AJAX functionality through the web interface
4. Verify the many-to-many relationships between users and categories
