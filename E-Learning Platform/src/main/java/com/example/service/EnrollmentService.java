package com.example.service;

import com.example.model.entities.Enrollment;
import com.example.model.entities.User;
import com.example.model.entities.Course;
import com.example.repository.EnrollmentRepository;
import com.example.repository.UserRepository;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import com.example.model.dto.EnrollmentResponse;
import com.example.model.dto.CreateEnrollmentRequest;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
@Service
public class EnrollmentService {
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    public Enrollment enroll(Long studentId, Long courseId) {
        User student = userRepository.findById(studentId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        return enrollmentRepository.save(enrollment);
    }

    public void unenroll(Long studentId, Long courseId) {
        Enrollment enrollment = enrollmentRepository.findByStudent_IdAndCourse_Id(studentId, courseId).orElseThrow();
        enrollmentRepository.delete(enrollment);
    }

    public List<Enrollment> findByStudent(Long studentId) {
        return enrollmentRepository.findByStudent_Id(studentId);
    }

    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream().map(this::toEnrollmentResponse).toList();
    }

    public Optional<EnrollmentResponse> getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).map(this::toEnrollmentResponse);
    }

    public EnrollmentResponse createEnrollment(CreateEnrollmentRequest request) {
        User student = userRepository.findById(request.getStudentId()).orElseThrow();
        Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setEnrolledAt(LocalDateTime.now());
        return toEnrollmentResponse(enrollmentRepository.save(enrollment));
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }

    private EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        EnrollmentResponse dto = new EnrollmentResponse();
        dto.setId(enrollment.getId());
        dto.setStudentId(enrollment.getStudent() != null ? enrollment.getStudent().getId() : null);
        dto.setStudentName(enrollment.getStudent() != null ? enrollment.getStudent().getName() : null);
        dto.setCourseId(enrollment.getCourse() != null ? enrollment.getCourse().getId() : null);
        dto.setCourseTitle(enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null);
        dto.setEnrolledAt(enrollment.getEnrolledAt() != null ? enrollment.getEnrolledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        return dto;
    }
}
