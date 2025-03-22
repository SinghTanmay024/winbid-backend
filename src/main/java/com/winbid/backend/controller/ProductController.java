package com.winbid.backend.controller;

import com.winbid.backend.model.Product;
import com.winbid.backend.model.ProductRequest;
import com.winbid.backend.model.Role;
import com.winbid.backend.model.User;
import com.winbid.backend.service.ProductService;
import com.winbid.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 NO CONTENT if no products
        }
        return ResponseEntity.ok(products); // HTTP 200 OK
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok) // HTTP 200 OK if found
                .orElseGet(() -> ResponseEntity.notFound().build()); // HTTP 404 NOT FOUND
    }

    // Create a new product
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        // Fetch user by ID
        User admin = userService.getUserById(productRequest.getAdmin().getId());

        // ✅ Check if the user is an admin
        if (admin.getRole() != Role.ADMIN) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null); // Or throw an exception
        }

        // Map ProductRequest to Product entity
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setTotalBids(productRequest.getTotalBids());
        product.setBidPrice(productRequest.getBidPrice());
        product.setOwner(admin);  // Set the admin (owner) to the product

        // Save the product
        Product savedProduct = productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    // Update product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product); // Directly return the Product
            return ResponseEntity.ok(updatedProduct); // HTTP 200 OK with updated product
        } catch (RuntimeException e) {
            // Handle the case when the product is not found
            return ResponseEntity.notFound().build(); // HTTP 404 NOT FOUND
        }
    }


    // Delete product by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        boolean deleted = productService.deleteProduct(id);
        return deleted ? ResponseEntity.noContent().build() // HTTP 204 NO CONTENT
                : ResponseEntity.notFound().build(); // HTTP 404 NOT FOUND
    }
}
