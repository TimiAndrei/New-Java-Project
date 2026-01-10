package com.example.service;

import com.example.model.entities.*;
import com.example.repository.CourseRepository;
import com.example.repository.CategoryRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import com.example.model.dto.CourseResponse;
import com.example.model.dto.CreateCourseRequest;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;

        public List<CourseResponse> getAllCourses() {
            List<Course> courses = courseRepository.findAll();
            System.out.println("DEBUG: Found " + courses.size() + " courses in DB");
            return courses.stream().map(this::toCourseResponse).toList();
        }

        public Optional<CourseResponse> getCourseById(Long id) {
            return courseRepository.findById(id).map(this::toCourseResponse);
        }

        public CourseResponse createCourse(CreateCourseRequest request) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
            User instructor = userRepository.findById(request.getInstructorId()).orElseThrow();
            Course course = new Course();
            course.setTitle(request.getTitle());
            course.setDescription(request.getDescription());
            course.setDifficulty(DifficultyLevel.valueOf(request.getDifficulty()));
            course.setCategory(category);
            course.setInstructor(instructor);
            course.setStatus(CourseStatus.DRAFT);
            return toCourseResponse(courseRepository.save(course));
        }

        public CourseResponse updateCourse(Long id, CreateCourseRequest request) {
            Course course = courseRepository.findById(id).orElseThrow();
            course.setTitle(request.getTitle());
            course.setDescription(request.getDescription());
            course.setDifficulty(DifficultyLevel.valueOf(request.getDifficulty()));
            course.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow());
            course.setInstructor(userRepository.findById(request.getInstructorId()).orElseThrow());
            return toCourseResponse(courseRepository.save(course));
        }

        public void deleteCourse(Long id) {
            courseRepository.deleteById(id);
        }

        private CourseResponse toCourseResponse(Course course) {
            CourseResponse dto = new CourseResponse();
            dto.setId(course.getId());
            dto.setTitle(course.getTitle());
            dto.setDescription(course.getDescription());
            dto.setDifficulty(course.getDifficulty() != null ? course.getDifficulty().name() : null);
            dto.setCategory(course.getCategory() != null ? course.getCategory().getName() : null);
            dto.setInstructor(course.getInstructor() != null ? course.getInstructor().getName() : null);
            dto.setStatus(course.getStatus() != null ? course.getStatus().name() : null);
            return dto;
        }

    public Course createCourse(String title, String description, DifficultyLevel difficulty, Long categoryId, Long instructorId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow();
        User instructor = userRepository.findById(instructorId).orElseThrow();
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setDifficulty(difficulty);
        course.setCategory(category);
        course.setInstructor(instructor);
        course.setStatus(CourseStatus.DRAFT);
        return courseRepository.save(course);
    }

    public List<Course> findPublishedCourses() {
        return courseRepository.findByStatus(CourseStatus.PUBLISHED);
    }

    public List<Course> searchCourses(String category, DifficultyLevel level, Long instructorId) {
        if (category != null) return courseRepository.findByCategory_Name(category);
        if (level != null) return courseRepository.findByDifficulty(level);
        if (instructorId != null) return courseRepository.findByInstructor_Id(instructorId);
        return courseRepository.findByStatus(CourseStatus.PUBLISHED);
    }

    public Course publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setStatus(CourseStatus.PUBLISHED);
        return courseRepository.save(course);
    }

    public Course archiveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setStatus(CourseStatus.ARCHIVED);
        return courseRepository.save(course);
    }
}
