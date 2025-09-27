package com.example.graphql.resolver;

import com.example.graphql.model.User;
import com.example.graphql.model.Category;
import com.example.graphql.repository.UserRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserResolver {
    
    private final UserRepository userRepository;
    
    public UserResolver(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @QueryMapping
    public List<User> allUsers() {
        return userRepository.findAll();
    }
    
    @QueryMapping
    public Optional<User> userById(@Argument Long id) {
        return userRepository.findById(id);
    }
    
    @QueryMapping
    public Optional<User> userByEmail(@Argument String email) {
        return userRepository.findByEmail(email);
    }
    
    @SchemaMapping
    public List<Category> categories(User user) {
        return user.getCategories().stream().toList();
    }
    
    @MutationMapping
    public User createUser(@Argument UserInput input) {
        User user = new User();
        user.setFullname(input.fullname());
        user.setEmail(input.email());
        user.setPassword(input.password());
        user.setPhone(input.phone());
        
        return userRepository.save(user);
    }
    
    @MutationMapping
    public User updateUser(@Argument Long id, @Argument UserInput input) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setFullname(input.fullname());
            user.setEmail(input.email());
            user.setPassword(input.password());
            user.setPhone(input.phone());
            
            return userRepository.save(user);
        }
        throw new RuntimeException("User not found with id: " + id);
    }
    
    @MutationMapping
    public Boolean deleteUser(@Argument Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public record UserInput(
        String fullname,
        String email,
        String password,
        String phone
    ) {}
}
