package com.example.service;

import com.example.model.dto.EnrollmentResponse;
import com.example.model.dto.CreateEnrollmentRequest;
import com.example.model.entities.Enrollment;
import com.example.model.entities.User;
import com.example.model.entities.Course;
import com.example.repository.EnrollmentRepository;
import com.example.repository.UserRepository;
import com.example.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.NoSuchElementException;

class EnrollmentServiceTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEnrollments_returnsList() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));
        List<EnrollmentResponse> result = enrollmentService.getAllEnrollments();
        assertEquals(1, result.size());
    }

    @Test
    void enroll_success() {
        User student = new User(); student.setId(2L);
        Course course = new Course(); course.setId(3L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> { Enrollment e = i.getArgument(0); e.setId(1L); return e; });
        Enrollment result = enrollmentService.enroll(2L, 3L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(student, result.getStudent());
        assertEquals(course, result.getCourse());
    }

    @Test
    void enroll_studentNotFound_throws() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> enrollmentService.enroll(2L, 3L));
    }

    @Test
    void enroll_courseNotFound_throws() {
        User student = new User();
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(3L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> enrollmentService.enroll(2L, 3L));
    }

    @Test
    void unenroll_success() {
        Enrollment enrollment = new Enrollment();
        when(enrollmentRepository.findByStudent_IdAndCourse_Id(2L, 3L)).thenReturn(Optional.of(enrollment));
        enrollmentService.unenroll(2L, 3L);
        verify(enrollmentRepository).delete(enrollment);
    }

    @Test
    void unenroll_notFound_throws() {
        when(enrollmentRepository.findByStudent_IdAndCourse_Id(2L, 3L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> enrollmentService.unenroll(2L, 3L));
    }

    @Test
    void deleteEnrollment_success() {
        enrollmentService.deleteEnrollment(1L);
        verify(enrollmentRepository).deleteById(1L);
    }

    @Test
    void createEnrollment_duplicate() {
        CreateEnrollmentRequest req = new CreateEnrollmentRequest();
        req.setStudentId(2L);
        req.setCourseId(3L);
        User student = new User();
        Course course = new Course();
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> { Enrollment e = i.getArgument(0); e.setId(1L); return e; });
        EnrollmentResponse res1 = enrollmentService.createEnrollment(req);
        assertNotNull(res1);
        // Simulez duplicate: nu ar trebui să arunce, dar se poate extinde logica pentru duplicate handling
        EnrollmentResponse res2 = enrollmentService.createEnrollment(req);
        assertNotNull(res2);
    }

    @Test
    void createEnrollment_success() {
        CreateEnrollmentRequest req = new CreateEnrollmentRequest();
        req.setStudentId(2L);
        req.setCourseId(3L);
        User student = new User();
        Course course = new Course();
        when(userRepository.findById(2L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(i -> { Enrollment e = i.getArgument(0); e.setId(1L); return e; });
        EnrollmentResponse res = enrollmentService.createEnrollment(req);
        assertNotNull(res);
    }
}
