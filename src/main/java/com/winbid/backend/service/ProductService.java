package com.winbid.backend.service;

import com.winbid.backend.model.Product;
import com.winbid.backend.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        // Find the product by ID
        return productRepository.findById(id).map(existingProduct -> {
            // Update fields with the new values
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());  // Set image URL
            existingProduct.setTotalBids(updatedProduct.getTotalBids());  // Set total bids
            existingProduct.setBidPrice(updatedProduct.getBidPrice());  // Set bid price

            // Optionally update the `updatedAt` timestamp
            existingProduct.setUpdatedAt(LocalDateTime.now());

            // Save and return the updated product
            return productRepository.save(existingProduct);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }



    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(product -> {
            productRepository.delete(product);
            return true;
        }).orElse(false);
    }
}
