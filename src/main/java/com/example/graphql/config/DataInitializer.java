package com.example.graphql.config;

import com.example.graphql.model.Category;
import com.example.graphql.model.User;
import com.example.graphql.model.Product;
import com.example.graphql.repository.CategoryRepository;
import com.example.graphql.repository.UserRepository;
import com.example.graphql.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public DataInitializer(CategoryRepository categoryRepository, 
                          UserRepository userRepository, 
                          ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create categories
        Category electronics = new Category();
        electronics.setName("Electronics");
        electronics.setImages("electronics.jpg");
        electronics = categoryRepository.save(electronics);
        
        Category clothing = new Category();
        clothing.setName("Clothing");
        clothing.setImages("clothing.jpg");
        clothing = categoryRepository.save(clothing);
        
        Category books = new Category();
        books.setName("Books");
        books.setImages("books.jpg");
        books = categoryRepository.save(books);
        
        // Create users
        User user1 = new User();
        user1.setFullname("John Doe");
        user1.setEmail("john@example.com");
        user1.setPassword("password123");
        user1.setPhone("123-456-7890");
        user1.setCategories(Set.of(electronics, books));
        user1 = userRepository.save(user1);
        
        User user2 = new User();
        user2.setFullname("Jane Smith");
        user2.setEmail("jane@example.com");
        user2.setPassword("password456");
        user2.setPhone("098-765-4321");
        user2.setCategories(Set.of(clothing, electronics));
        user2 = userRepository.save(user2);
        
        User user3 = new User();
        user3.setFullname("Bob Johnson");
        user3.setEmail("bob@example.com");
        user3.setPassword("password789");
        user3.setPhone("555-123-4567");
        user3.setCategories(Set.of(books, clothing));
        user3 = userRepository.save(user3);
        
        // Create products
        Product product1 = new Product();
        product1.setTitle("iPhone 15");
        product1.setQuantity(50);
        product1.setDescription("Latest iPhone with advanced features");
        product1.setPrice(new BigDecimal("999.99"));
        product1.setUser(user1);
        productRepository.save(product1);
        
        Product product2 = new Product();
        product2.setTitle("Samsung Galaxy S24");
        product2.setQuantity(30);
        product2.setDescription("Premium Android smartphone");
        product2.setPrice(new BigDecimal("899.99"));
        product2.setUser(user1);
        productRepository.save(product2);
        
        Product product3 = new Product();
        product3.setTitle("Nike Air Max");
        product3.setQuantity(100);
        product3.setDescription("Comfortable running shoes");
        product3.setPrice(new BigDecimal("129.99"));
        product3.setUser(user2);
        productRepository.save(product3);
        
        Product product4 = new Product();
        product4.setTitle("Adidas T-Shirt");
        product4.setQuantity(200);
        product4.setDescription("Comfortable cotton t-shirt");
        product4.setPrice(new BigDecimal("29.99"));
        product4.setUser(user2);
        productRepository.save(product4);
        
        Product product5 = new Product();
        product5.setTitle("Spring Boot Guide");
        product5.setQuantity(75);
        product5.setDescription("Complete guide to Spring Boot development");
        product5.setPrice(new BigDecimal("49.99"));
        product5.setUser(user3);
        productRepository.save(product5);
        
        Product product6 = new Product();
        product6.setTitle("GraphQL Handbook");
        product6.setQuantity(60);
        product6.setDescription("Learn GraphQL from basics to advanced");
        product6.setPrice(new BigDecimal("39.99"));
        product6.setUser(user3);
        productRepository.save(product6);
        
        System.out.println("Sample data initialized successfully!");
    }
}
