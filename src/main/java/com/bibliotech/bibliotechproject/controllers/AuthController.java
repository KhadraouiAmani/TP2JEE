package com.bibliotech.bibliotechproject.controllers;

import com.bibliotech.bibliotechproject.dtos.AuthResponse;
import com.bibliotech.bibliotechproject.dtos.LoginRequest;
import com.bibliotech.bibliotechproject.repositories.UserRepository;
import com.bibliotech.bibliotechproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired private JwtUtil jwtUtil;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody LoginRequest request) {
        return userRepository.findByUsername(request.getUsername())
                .doOnNext(user -> System.out.println("DEBUG: Found user in DB: " + user.getUsername()))
                .filter(user -> {
                    boolean match = passwordEncoder.matches(request.getPassword(), user.getPassword());
                    System.out.println("DEBUG: Password match result: " + match);
                    return match;
                })
                .map(user -> new AuthResponse(jwtUtil.generateToken(user.getUsername())))
                .switchIfEmpty(Mono.defer(() -> {
                    System.out.println("DEBUG: Login failed for user: " + request.getUsername());
                    return Mono.error(new RuntimeException("Invalid username or password!"));
                }));
    }
}