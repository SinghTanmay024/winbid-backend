package com.winbid.backend.controller;

import com.winbid.backend.model.User;
import com.winbid.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Create - Register a new user
    @PostMapping("/register")
    public ResponseEntity<User> addUser(@RequestBody @Valid User user) {
        User savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
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
}
