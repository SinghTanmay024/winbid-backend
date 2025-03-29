package com.winbid.backend.controller;

import com.winbid.backend.model.JwtResponse;
import com.winbid.backend.model.LoginRequest;
import com.winbid.backend.model.User;
import com.winbid.backend.repositories.UserRepository;
import com.winbid.backend.security.JwtUtil;
import com.winbid.backend.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // User registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        // Check if the user already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        try {
            // Encode the password before saving it
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Save the user to the database
            userRepository.save(user);

            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);  // Return 201 status for successful creation
        } catch (Exception e) {
            log.error("Exception occurred during user registration", e);
            return new ResponseEntity<>("Error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Check if the user exists in the database
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        // Authenticate using AuthenticationManager
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Set authentication context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            String jwt = jwtUtil.generateToken(email);

            // Return JWT token in the response
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (Exception e) {
            // Log the error and provide a generic message
            log.error("Authentication failed for email: {}", email, e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }


    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        // Clear authentication context (invalidate session)
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }
    @GetMapping("/validate")
    public ResponseEntity<String> validate(Authentication authentication) {
        try {
            // Check if the user is authenticated
            if (authentication != null && authentication.isAuthenticated()) {
                // User is authenticated
                String username = authentication.getName();
                return ResponseEntity.ok("Token is valid for user: " + username);
            } else {
                // No valid authentication found
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (Exception e) {
            log.error("Error during token validation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during validation");
        }
    }
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Don't return password in the response
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

}