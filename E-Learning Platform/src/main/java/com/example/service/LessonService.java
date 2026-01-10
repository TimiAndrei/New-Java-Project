package com.example.service;

import com.example.model.entities.Lesson;
import com.example.model.entities.Course;
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

    @Autowired
    public LessonService(LessonRepository lessonRepository, CourseRepository courseRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
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
