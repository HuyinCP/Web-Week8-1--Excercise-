package com.example.graphql.resolver;

import com.example.graphql.model.Category;
import com.example.graphql.model.User;
import com.example.graphql.repository.CategoryRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class CategoryResolver {
    
    private final CategoryRepository categoryRepository;
    
    public CategoryResolver(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    
    @QueryMapping
    public List<Category> allCategories() {
        return categoryRepository.findAll();
    }
    
    @QueryMapping
    public Optional<Category> categoryById(@Argument Long id) {
        return categoryRepository.findById(id);
    }
    
    @SchemaMapping
    public List<User> users(Category category) {
        return category.getUsers().stream().toList();
    }
    
    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        Category category = new Category();
        category.setName(input.name());
        category.setImages(input.images());
        
        return categoryRepository.save(category);
    }
    
    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Optional<Category> existingCategory = categoryRepository.findById(id);
        if (existingCategory.isPresent()) {
            Category category = existingCategory.get();
            category.setName(input.name());
            category.setImages(input.images());
            
            return categoryRepository.save(category);
        }
        throw new RuntimeException("Category not found with id: " + id);
    }
    
    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public record CategoryInput(
        String name,
        String images
    ) {}
}
