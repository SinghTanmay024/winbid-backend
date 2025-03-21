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
    private UserRepository userRepository;

    // Create user
    public User saveUser(User user) {
        return userRepository.save(user);
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
