package com.example.controller;

import com.example.model.dto.UserResponse;
import com.example.model.dto.CreateUserRequest;
import com.example.service.UserService;
import com.example.model.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody CreateUserRequest request) {
        // Only allow STUDENT role for public registration
        Set<String> roles = new HashSet<>();
        roles.add("STUDENT");
        User user = userService.register(request.getEmail(), request.getPassword(), request.getName(), roles);
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setRoles(roles);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");
        return userService.authenticate(email, password)
            .map(user -> {
                UserResponse response = new UserResponse();
                response.setId(user.getId());
                response.setEmail(user.getEmail());
                response.setName(user.getName());
                response.setRoles(user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()));
                return response;
            })
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(401).build());
    }
}
