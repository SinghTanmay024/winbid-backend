package com.winbid.backend.service;

import com.winbid.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.winbid.backend.model.User;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired  // Make sure you're using constructor injection
    public UserService(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    // Create user
    public User saveUser(User user) {
        User savedUser = userRepository.save(user);

        // Send thank you email
        try {
            System.out.println("Before sending email");  // Simple debug
            emailService.sendThankYouEmail(savedUser.getEmail(), savedUser.getFirstName());
            System.out.println("After sending email");  // Simple debug
        } catch (Exception e) {
            // Log the error but don't fail the registration
            System.err.println("Failed to send thank you email: " + e.getMessage());
        }

        return savedUser;
    }

    // Get user by email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Update user
    public Optional<User> updateUser(String email, User updatedUser) {
        return userRepository.findByEmail(email).map(existingUser -> {
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setRole(updatedUser.getRole());
            return userRepository.save(existingUser);
        });
    }

    // Delete user by email
    public boolean deleteUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    // Delete all users
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
