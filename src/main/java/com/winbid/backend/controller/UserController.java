package com.winbid.backend.controller;

import com.winbid.backend.model.Bid;
import com.winbid.backend.model.User;
import com.winbid.backend.repositories.BidRepository;
import com.winbid.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BidRepository bidRepository;

    // Create - Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
    // Read - Get user by email
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        Optional<User> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok) // HTTP 200 OK if found
                .orElseGet(() -> ResponseEntity.notFound().build()); // HTTP 404 NOT FOUND
    }

    // Read - Get all users
    @GetMapping("/")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build(); // HTTP 204 NO CONTENT
        }
        return ResponseEntity.ok(users); // HTTP 200 OK
    }

    // Update - Update user details
    @PutMapping("/{email}")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(email, user);
        return updatedUser.map(ResponseEntity::ok) // HTTP 200 OK
                .orElseGet(() -> ResponseEntity.notFound().build()); // HTTP 404 NOT FOUND
    }

    // Delete - Delete user by email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        return deleted ? ResponseEntity.noContent().build() // HTTP 204 NO CONTENT
                : ResponseEntity.notFound().build(); // HTTP 404 NOT FOUND
    }

    // Delete - Delete all users (Optional)
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAllUsers() {
        userService.deleteAllUsers();
        return ResponseEntity.noContent().build(); // HTTP 204 NO CONTENT
    }
    @GetMapping("/bids/{userId}")
    public ResponseEntity<Integer> numberOfBids(@PathVariable Long userId) {
        List<Bid> bids = bidRepository.findByUserId(userId);
        return ResponseEntity.ok(bids.size());
    }
    @GetMapping("/bidproduct/{userId}")
    public ResponseEntity<List<Bid>> BidProduct(@PathVariable Long userId) {
        List<Bid> bids = bidRepository.findByUserId(userId);
        return ResponseEntity.ok(bids);
    }
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {

        System.out.println("Authenticating user: " + userDetails.getUsername());

        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email: " + email));

        System.out.println("Found user: " + user.getEmail());

        List<Bid> bids = bidRepository.findByUserId(user.getId());
        System.out.println("User has " + bids.size() + " bids");

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("bids", bids);

        return ResponseEntity.ok(response);
    }
}
