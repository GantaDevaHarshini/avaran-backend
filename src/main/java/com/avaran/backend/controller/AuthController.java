package com.avaran.backend.controller;

import com.avaran.backend.model.User;
import com.avaran.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:3000}")
public class AuthController {

    private final UserRepository repo;

    public AuthController(UserRepository repo) {
        this.repo = repo;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (isBlank(user.getName()) || isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Name, email, and password are required."));
        }

        if (repo.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Email is already registered."));
        }

        User savedUser = repo.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (isBlank(user.getEmail()) || isBlank(user.getPassword())) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Email and password are required."));
        }

        User existing = repo.findByEmail(user.getEmail());

        if (existing == null || !existing.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials."));
        }

        return ResponseEntity.ok(UserResponse.from(existing));
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private record UserResponse(Long id, String name, String email) {
        private static UserResponse from(User user) {
            return new UserResponse(user.getId(), user.getName(), user.getEmail());
        }
    }

    private record ErrorResponse(String message) {
    }
}
