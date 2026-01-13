package com.example.controller;

import com.example.model.entities.Category;
import com.example.model.entities.DifficultyLevel;
import com.example.model.entities.User;
import com.example.repository.CategoryRepository;
import com.example.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import com.example.model.dto.CourseResponse;
import com.example.model.dto.CreateCourseRequest;
import com.example.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/courses")

public class CourseController {
    private final CourseService courseService;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CourseController(CourseService courseService, CategoryRepository categoryRepository, UserRepository userRepository) {
        this.courseService = courseService;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }


    @GetMapping
    public List<CourseResponse> getAllCourses() {
        // Optionally, get userId from authentication for enrolled info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof com.example.security.CustomUserDetails userDetails) {
                userId = userDetails.getUser().getId();
            } else {
                String email = auth.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) userId = user.getId();
            }
        }
        return courseService.getAllCourses(userId);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @GetMapping("/difficulties")
    public DifficultyLevel[] getAllDifficulties() {
        return DifficultyLevel.values();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.of(courseService.getCourseById(id));
    }


    @PostMapping
    public CourseResponse createCourse(@RequestBody CreateCourseRequest request) {
        // Set instructor from authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new RuntimeException("Not authenticated");
        }
        Object principal = auth.getPrincipal();
        Long instructorId = null;
        if (principal instanceof com.example.security.CustomUserDetails userDetails) {
            instructorId = userDetails.getUser().getId();
        } else {
            // fallback: try by email
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            instructorId = user.getId();
        }
        request.setInstructorId(instructorId);
        return courseService.createCourse(request);
    }

    @PutMapping("/{id}")
    public CourseResponse updateCourse(@PathVariable Long id, @RequestBody CreateCourseRequest request) {
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping("/{id}/publish")
    public ResponseEntity<?> publishCourse(@PathVariable Long id) {
        // Only instructors can publish
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(403).body("Not authenticated");
        }
        Object principal = auth.getPrincipal();
        Long userId = null;
        if (principal instanceof com.example.security.CustomUserDetails userDetails) {
            userId = userDetails.getUser().getId();
        } else {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) userId = user.getId();
        }
        if (userId == null || !courseService.isInstructorOfCourse(userId, id)) {
            return ResponseEntity.status(403).body("Only the instructor can publish this course");
        }
        return ResponseEntity.ok(courseService.publishCourse(id));
    }

    @PostMapping("/{id}/archive")
    public ResponseEntity<?> archiveCourse(@PathVariable Long id) {
        // Only instructors can archive
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(403).body("Not authenticated");
        }
        Object principal = auth.getPrincipal();
        Long userId = null;
        if (principal instanceof com.example.security.CustomUserDetails userDetails) {
            userId = userDetails.getUser().getId();
        } else {
            String email = auth.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) userId = user.getId();
        }
        if (userId == null || !courseService.isInstructorOfCourse(userId, id)) {
            return ResponseEntity.status(403).body("Only the instructor can archive this course");
        }
        return ResponseEntity.ok(courseService.archiveCourse(id));
    }
}
