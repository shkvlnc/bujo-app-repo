package com.shkvlnc.bujo_app.web;


import com.shkvlnc.bujo_app.dto.login.AuthResponse;
import com.shkvlnc.bujo_app.dto.login.LoginRequest;
import com.shkvlnc.bujo_app.service.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new AuthResponse(token)); // ✅ returns JSON
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new AuthResponse("Login failed: " + e.getMessage()));
        }
    }

}

