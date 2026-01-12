package com.example.controller;

import com.example.model.dto.CourseResponse;
import com.example.model.entities.Lesson;
import com.example.model.entities.User;
import com.example.repository.UserRepository;
import com.example.service.CourseService;
import com.example.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CoursePageController {
    private final CourseService courseService;
    private final LessonService lessonService;
    private final UserRepository userRepository;

    @Autowired
    public CoursePageController(CourseService courseService, LessonService lessonService, UserRepository userRepository) {
        this.courseService = courseService;
        this.lessonService = lessonService;
        this.userRepository = userRepository;
    }

    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String displayName = null;
        boolean isInstructor = false;
        boolean isAdmin = false;
        boolean isEnrolled = false;
        Long userId = null;
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            Object principal = auth.getPrincipal();
            if (principal instanceof com.example.security.CustomUserDetails userDetails) {
                userId = userDetails.getUser().getId();
                displayName = userDetails.getUser().getName();
                isInstructor = userDetails.getUser().getRoles().stream().anyMatch(r -> r.getName().equals("INSTRUCTOR"));
                isAdmin = userDetails.getUser().getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
            } else {
                String email = auth.getName();
                User user = userRepository.findByEmail(email).orElse(null);
                if (user != null) {
                    userId = user.getId();
                    displayName = user.getName();
                    isInstructor = user.getRoles().stream().anyMatch(r -> r.getName().equals("INSTRUCTOR"));
                    isAdmin = user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN"));
                }
            }
        }
        CourseResponse course = courseService.getCourseById(id).orElse(null);
        if (course == null) return "redirect:/courses";
        if (userId != null && course.isEnrolled()) isEnrolled = true;
        List<Lesson> lessons = lessonService.getLessonsByCourse(id);
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("displayName", displayName);
        model.addAttribute("isInstructor", isInstructor);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isEnrolled", isEnrolled);
        return "course_details";
    }
}
