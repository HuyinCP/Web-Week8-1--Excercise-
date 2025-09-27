package com.example.graphql.repository;

import com.example.graphql.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    List<Product> findByOrderByPriceAsc();
    
    @Query("SELECT p FROM Product p JOIN p.user u JOIN u.categories c WHERE c.id = :categoryId")
    List<Product> findByCategoryId(Long categoryId);
    
    List<Product> findByTitleContainingIgnoreCase(String title);
}
