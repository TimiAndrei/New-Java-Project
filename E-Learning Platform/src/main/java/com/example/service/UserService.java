package com.example.service;

import com.example.model.entities.User;
import com.example.model.entities.Role;
import com.example.model.dto.UserResponse;
import com.example.model.dto.CreateUserRequest;
import java.util.List;
import com.example.repository.UserRepository;
import com.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    public UserResponse updateUserRoles(Long id, java.util.Set<String> roles) {
        User user = userRepository.findById(id).orElseThrow();
        Set<Role> newRoles = new HashSet<>();
        for (String roleName : roles) {
            Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
            newRoles.add(role);
        }
        user.setRoles(newRoles);
        return toUserResponse(userRepository.save(user));
    }

        public User getCurrentUser(org.springframework.security.core.Authentication auth) {
            String email = auth.getName();
            return userRepository.findByEmail(email).orElseThrow();
        }

        public boolean isAdmin(User user) {
            return user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
        }

        public boolean isInstructor(User user) {
            return user.getRoles().stream().anyMatch(r -> r.getName().equals("INSTRUCTOR"));
        }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

        public List<UserResponse> getAllUsers() {
            return userRepository.findAll().stream().map(this::toUserResponse).toList();
        }

        public java.util.Optional<UserResponse> getUserById(Long id) {
            return userRepository.findById(id).map(this::toUserResponse);
        }

        public UserResponse createUser(CreateUserRequest request) {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setName(request.getName());
            Set<Role> roles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
            return toUserResponse(userRepository.save(user));
        }

        public UserResponse updateUser(Long id, CreateUserRequest request) {
            User user = userRepository.findById(id).orElseThrow();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            }
            Set<Role> roles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
                roles.add(role);
            }
            user.setRoles(roles);
            return toUserResponse(userRepository.save(user));
        }

        public void deleteUser(Long id) {
            userRepository.deleteById(id);
        }

        private UserResponse toUserResponse(User user) {
            UserResponse dto = new UserResponse();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            dto.setRoles(user.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()));
            return dto;
        }

    public User register(String email, String password, String name, Set<String> roleNames) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName).orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));
            roles.add(role);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public Optional<User> authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return userOpt;
        }
        return Optional.empty();
    }

    public User updateProfile(Long userId, String name) {
        User user = userRepository.findById(userId).orElseThrow();
        user.setName(name);
        return userRepository.save(user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
