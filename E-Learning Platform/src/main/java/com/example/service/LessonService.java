package com.example.service;

import com.example.model.entities.Lesson;
import com.example.model.entities.Course;
import com.example.model.entities.Quiz;
import com.example.repository.LessonRepository;
import com.example.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;

    // ...existing code...

    // Assign a quiz to a lesson
    public void setQuizForLesson(Long lessonId, Quiz quiz) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setQuiz(quiz);
        lessonRepository.save(lesson);
    }

    // Remove quiz from lesson
    public void removeQuizFromLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow();
        lesson.setQuiz(null);
        lessonRepository.save(lesson);
    }

    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
    }

    // List lessons by course
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourse_Id(courseId);
    }

    // Check if current user is instructor of the course
    public boolean isInstructorOfCourse(Long courseId) {
        org.springframework.security.core.Authentication auth = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        Object principal = auth.getPrincipal();
        Long userId = null;
        if (principal instanceof com.example.security.CustomUserDetails userDetails) {
            userId = userDetails.getUser().getId();
        } else {
            // Remove unused variable 'email'
            com.example.model.entities.User user = courseRepository.findById(courseId).map(Course::getInstructor).orElse(null);
            if (user != null) userId = user.getId();
        }
        final Long finalUserId = userId;
        return courseRepository.findById(courseId)
                .map(c -> c.getInstructor() != null && c.getInstructor().getId().equals(finalUserId))
                .orElse(false);
    }

    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    public Optional<Lesson> getLessonById(Long id) {
        return lessonRepository.findById(id);
    }

    public Lesson createLesson(com.example.model.dto.CreateLessonRequest request) {
        Lesson lesson = new Lesson();
        lesson.setTitle(request.getTitle());
        lesson.setContent(request.getContent());
        lesson.setLessonOrder(request.getLessonOrder());
        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
            lesson.setCourse(course);
        }
        return lessonRepository.save(lesson);
    }

    public Optional<Lesson> updateLesson(Long id, com.example.model.dto.CreateLessonRequest request) {
        return lessonRepository.findById(id).map(existing -> {
            existing.setTitle(request.getTitle());
            existing.setContent(request.getContent());
            existing.setLessonOrder(request.getLessonOrder());
            if (request.getCourseId() != null) {
                Course course = courseRepository.findById(request.getCourseId()).orElseThrow();
                existing.setCourse(course);
            }
            return lessonRepository.save(existing);
        });
    }

    public boolean deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
