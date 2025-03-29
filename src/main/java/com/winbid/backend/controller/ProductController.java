package com.winbid.backend.controller;

import com.winbid.backend.model.*;
import com.winbid.backend.repositories.BidRepository;
import com.winbid.backend.repositories.ProductRepository;
import com.winbid.backend.repositories.UserRepository;
import com.winbid.backend.repositories.WinnerRepository;
import com.winbid.backend.service.CloudinaryService;
import com.winbid.backend.service.ProductService;
import com.winbid.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private WinnerRepository winnerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ProductRepository productRepository;

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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductRequest productRequest,
            BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        // Find the owner
        User owner = userRepository.findById(productRequest.getUserId())
                .orElseThrow(() -> new IOException("User not found"));

        // Upload image to Cloudinary if present
        String imageUrl = null;
        if (productRequest.getImageFile() != null && !productRequest.getImageFile().isEmpty()) {
            imageUrl = cloudinaryService.uploadFile(productRequest.getImageFile());
        }

        // Create product
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(imageUrl);
        product.setTotalBids(productRequest.getTotalBids());
        product.setBidPrice(productRequest.getBidPrice());
        product.setOwner(owner);

        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
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
    @GetMapping("/bid/{id}")
    public ResponseEntity<Integer> completedBids(@PathVariable Long id){
        List<Bid> bids = bidRepository.findByProductId(id);
        return new ResponseEntity<>(bids.size(),HttpStatus.OK);
    }

    @GetMapping("/winner/{id}")
    public ResponseEntity<Long> productWinner(@PathVariable Long id){
        Long userId = 0L;
        Winner winner = winnerRepository.findByProductId(id);
        if(winner != null){
            userId = winner.getUser().getId();;
        }
        return new ResponseEntity<>(userId,HttpStatus.OK);
    }
}
