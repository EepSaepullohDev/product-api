package com.bts.product.service;

import com.bts.product.dto.ProductDTO;
import com.bts.product.entity.Product;
import com.bts.product.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Get all products with filters and pagination
    @Cacheable(value = "products", key = "#search + '-' + #category + '-' + #limit + '-' + #page")
    public Page<Product> getAllProducts(String search, String category, int limit, int page) {
        Pageable pageable = PageRequest.of(page - 1, limit); // page - 1 karena Spring mulai dari 0
        return productRepository.findWithFilters(search, category, pageable);
    }

    // Get product by ID
    @Cacheable(value = "product", key = "#id")
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    // Create new product
    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public Product createProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());

        // Convert images list to JSON string
        try {
            String imagesJson = objectMapper.writeValueAsString(productDTO.getImages());
            product.setImages(imagesJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting images to JSON");
        }

        // Set created by info from authenticated user
        String username = getCurrentUsername();
        product.setCreatedBy(username);
        product.setCreatedById(1L); // Simplified - bisa diganti dengan user ID dari DB
        product.setUpdatedBy(username);
        product.setUpdatedById(1L);

        return productRepository.save(product);
    }

    // Update product
    public Product updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        product.setTitle(productDTO.getTitle());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());

        // Convert images list to JSON string
        try {
            String imagesJson = objectMapper.writeValueAsString(productDTO.getImages());
            product.setImages(imagesJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting images to JSON");
        }

        // Set updated by info
        String username = getCurrentUsername();
        product.setUpdatedBy(username);
        product.setUpdatedById(1L);

        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    // Helper method to get current username from security context
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "anonymous";
    }
}
