package com.example.controller;

import com.example.model.entities.Course;
import com.example.model.entities.User;
import com.example.service.CourseService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class MyCoursesController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    @GetMapping("/my-courses")
    public String myCourses(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getCurrentUser(auth);
        boolean isInstructor = userService.isInstructor(user);
        model.addAttribute("isInstructor", isInstructor);
        model.addAttribute("displayName", user.getName());
        if (isInstructor) {
            List<Course> ownedCourses = courseService.getCoursesByInstructor(user.getId());
            model.addAttribute("ownedCourses", ownedCourses);
        } else {
            List<Course> enrolledCourses = courseService.getCoursesByStudent(user.getId());
            model.addAttribute("enrolledCourses", enrolledCourses);
        }
        model.addAttribute("isAdmin", userService.isAdmin(user));
        return "my_courses";
    }
}
