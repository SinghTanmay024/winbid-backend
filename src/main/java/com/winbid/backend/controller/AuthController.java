package com.winbid.backend.controller;

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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
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
    private PasswordEncoder passwordEncoder;  // Inject PasswordEncoder

    // User registration endpoint
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
//        // Check if the user already exists
//        if (userRepository.existsByEmail(user.getEmail())) {
//            return ResponseEntity.badRequest().body("Email already in use");
//        }
//
//        // Encode the password before saving it to the database
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        // Save the user to the database
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully");
        try{
            authenticationManager.authenticate
                    (new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
            String jwt = jwtUtil.generateToken(userDetails.getUsername());
            return new  ResponseEntity<>(jwt,HttpStatus.OK);
        }catch (Exception e){
            log.error("Exception occured while creating token");
            return new  ResponseEntity<>("Incorrect username or password",HttpStatus.BAD_REQUEST);
        }
    }

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        // Check if the user exists in the database
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Compare entered password with stored password (encoded)
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ResponseEntity.badRequest().body("Bad credentials");
        }

        // Authenticate using AuthenticationManager if password matches
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // Set authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtUtil.generateToken(email);
        return ResponseEntity.ok(jwt);
    }
}

