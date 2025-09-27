package com.example.graphql.resolver;

import com.example.graphql.model.Product;
import com.example.graphql.model.User;
import com.example.graphql.repository.ProductRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ProductResolver {
    
    private final ProductRepository productRepository;
    
    public ProductResolver(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @QueryMapping
    public List<Product> allProducts() {
        return productRepository.findByOrderByPriceAsc();
    }
    
    @QueryMapping
    public Optional<Product> productById(@Argument Long id) {
        return productRepository.findById(id);
    }
    
    @QueryMapping
    public List<Product> productsByCategory(@Argument Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    @SchemaMapping
    public User user(Product product) {
        return product.getUser();
    }
    
    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Product product = new Product();
        product.setTitle(input.title());
        product.setQuantity(input.quantity());
        product.setDescription(input.description());
        product.setPrice(input.price());
        
        User user = new User();
        user.setId(input.userId());
        product.setUser(user);
        
        return productRepository.save(product);
    }
    
    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setTitle(input.title());
            product.setQuantity(input.quantity());
            product.setDescription(input.description());
            product.setPrice(input.price());
            
            User user = new User();
            user.setId(input.userId());
            product.setUser(user);
            
            return productRepository.save(product);
        }
        throw new RuntimeException("Product not found with id: " + id);
    }
    
    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public record ProductInput(
        String title,
        Integer quantity,
        String description,
        java.math.BigDecimal price,
        Long userId
    ) {}
}
