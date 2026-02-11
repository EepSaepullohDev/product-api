package com.bts.product.service;

import com.bts.product.dto.AuthResponse;
import com.bts.product.dto.LoginRequest;
import com.bts.product.dto.RegisterRequest;
import com.bts.product.entity.User;
import com.bts.product.repository.UserRepository;
import com.bts.product.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public User register(RegisterRequest request) {
        // Validate password confirmation
        if (!request.getPassword().equals(request.getPasswordConfirmation())) {
            throw new RuntimeException("Passwords do not match");
        }

        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Authenticate
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Generate tokens
        String token = jwtUtil.generateToken(request.getUsername());
        String refreshToken = jwtUtil.generateToken(request.getUsername()); // Simplified

        return new AuthResponse(token, refreshToken, request.getUsername());
    }
}
