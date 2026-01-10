package com.example.service;

import com.example.model.dto.CourseResponse;
import com.example.model.dto.CreateCourseRequest;
import com.example.model.entities.Category;
import com.example.model.entities.Course;
import com.example.model.entities.User;
import com.example.repository.CategoryRepository;
import com.example.repository.CourseRepository;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCourses_returnsList() {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findAll()).thenReturn(List.of(course));
        List<CourseResponse> result = courseService.getAllCourses();
        assertEquals(1, result.size());
    }

    @Test
    void getCourseById_found() {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Optional<CourseResponse> result = courseService.getCourseById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    void createCourse_success() {
        CreateCourseRequest req = new CreateCourseRequest();
        req.setTitle("Test");
        req.setDescription("Desc");
        req.setCategoryId(2L);
        req.setInstructorId(3L);
        req.setDifficulty("BEGINNER");
        Category cat = new Category();
        User instructor = new User();
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat));
        when(userRepository.findById(3L)).thenReturn(Optional.of(instructor));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> { Course c = i.getArgument(0); c.setId(1L); return c; });
        CourseResponse res = courseService.createCourse(req);
        assertEquals("Test", res.getTitle());
    }
}
