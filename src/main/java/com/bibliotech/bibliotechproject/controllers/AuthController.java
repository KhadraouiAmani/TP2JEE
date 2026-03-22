package com.bibliotech.bibliotechproject.controllers;

import com.bibliotech.bibliotechproject.dtos.AuthResponse;
import com.bibliotech.bibliotechproject.dtos.LoginRequest;
import com.bibliotech.bibliotechproject.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Mono<AuthResponse> login(@RequestBody LoginRequest request) {
        // Simple login: gives a token to anyone who asks for testing
        String token = jwtUtil.generateToken(request.getUsername());
        return Mono.just(new AuthResponse(token));
    }
}