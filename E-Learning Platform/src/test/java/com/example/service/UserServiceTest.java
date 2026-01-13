package com.example.service;

import com.example.model.dto.CreateUserRequest;
import com.example.model.dto.UserResponse;
import com.example.model.entities.Role;
import com.example.model.entities.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
        @Test
        void updateUser_success() {
            CreateUserRequest req = new CreateUserRequest();
            req.setEmail("test@example.com");
            req.setPassword("pass");
            req.setName("Updated User");
            req.setRoles(Set.of("USER"));
            User user = new User();
            user.setId(1L);
            user.setEmail("test@example.com");
            user.setName("Updated User");
            Role role = new Role();
            role.setName("USER");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
            when(passwordEncoder.encode(any())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(user);
            UserResponse res = userService.updateUser(1L, req);
            assertEquals("Updated User", res.getName());
        }

        @Test
        void updateUserRoles_success() {
            User user = new User();
            user.setId(1L);
            Role role = new Role();
            role.setName("ADMIN");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(role));
            when(userRepository.save(any(User.class))).thenReturn(user);
            UserResponse res = userService.updateUserRoles(1L, Set.of("ADMIN"));
            assertTrue(res.getRoles().contains("ADMIN"));
        }

        @Test
        void deleteUser_success() {
            userService.deleteUser(1L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        void register_success() {
            Role role = new Role();
            role.setName("USER");
            when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
            when(passwordEncoder.encode(any())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(i -> { User u = i.getArgument(0); u.setId(2L); return u; });
            User user = userService.register("mail", "pass", "Name", Set.of("USER"));
            assertEquals("mail", user.getEmail());
            assertEquals("Name", user.getName());
        }

        @Test
        void authenticate_success() {
            User user = new User();
            user.setEmail("mail");
            user.setPassword("encoded");
            when(userRepository.findByEmail("mail")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
            Optional<User> result = userService.authenticate("mail", "pass");
            assertTrue(result.isPresent());
        }

        @Test
        void updateProfile_success() {
            User user = new User();
            user.setId(1L);
            user.setName("Old");
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userRepository.save(any(User.class))).thenReturn(user);
            User updated = userService.updateProfile(1L, "New");
            assertEquals("New", updated.getName());
        }
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_success() {
        CreateUserRequest req = new CreateUserRequest();
        req.setEmail("test@example.com");
        req.setPassword("pass");
        req.setName("Test User");
        req.setRoles(Set.of("USER"));

        Role role = new Role();
        role.setName("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(i -> {
            User u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UserResponse res = userService.createUser(req);
        assertEquals("test@example.com", res.getEmail());
        assertEquals("Test User", res.getName());
        assertTrue(res.getRoles().contains("USER"));
    }
}
