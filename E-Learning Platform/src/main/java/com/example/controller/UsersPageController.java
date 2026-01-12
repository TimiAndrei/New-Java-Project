package com.example.controller;

import com.example.model.entities.User;
import com.example.model.entities.Role;
import com.example.service.UserService;
import com.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Set;

@Controller
public class UsersPageController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping("/users")
    public String usersPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentUser(auth);
        model.addAttribute("isAdmin", userService.isAdmin(user));
        model.addAttribute("displayName", user.getName());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleRepository.findAll().stream().map(Role::getName).toList());
        return "users";
    }
}
