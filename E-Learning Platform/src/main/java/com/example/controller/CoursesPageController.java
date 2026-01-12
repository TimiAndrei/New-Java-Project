package com.example.controller;

import com.example.service.CourseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CoursesPageController {
    private final CourseService courseService;

    @Autowired
    public CoursesPageController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public String showCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        String displayName = null;
        boolean isInstructor = false;
        boolean isAdmin = false;
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof com.example.security.CustomUserDetails userDetails) {
                userId = userDetails.getUser().getId();
                displayName = userDetails.getName();
                isInstructor = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_INSTRUCTOR"));
                isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            } else {
                displayName = auth.getName();
            }
        }
        model.addAttribute("courses", courseService.getAllCourses(userId));
        model.addAttribute("displayName", displayName);
        model.addAttribute("isInstructor", isInstructor);
        model.addAttribute("isAdmin", isAdmin);
        return "courses";
    }
}
