package com.example.controller;

import com.example.model.dto.EnrollmentResponse;
import com.example.model.dto.CreateEnrollmentRequest;
import com.example.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @Autowired
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentResponse> getEnrollmentById(@PathVariable Long id) {
        return ResponseEntity.of(enrollmentService.getEnrollmentById(id));
    }

    @PostMapping
    public EnrollmentResponse createEnrollment(@RequestBody CreateEnrollmentRequest request, org.springframework.security.core.Authentication authentication) {
        if (request.getStudentId() == null && authentication != null) {
            // Use authenticated user as student
            Object principal = authentication.getPrincipal();
            if (principal instanceof com.example.security.CustomUserDetails userDetails) {
                com.example.model.entities.User user = userDetails.getUser();
                request.setStudentId(user.getId());
            } else {
                throw new RuntimeException("Authenticated principal is not CustomUserDetails");
            }
        }
        return enrollmentService.createEnrollment(request);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
    }
}
