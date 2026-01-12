
package com.example.controller;

import com.example.model.entities.Lesson;
import com.example.model.dto.CreateLessonRequest;
import com.example.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonService lessonService;
    @Autowired
    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    // List all lessons for a course
    @GetMapping("/by-course/{courseId}")
    public List<Lesson> getLessonsByCourse(@PathVariable Long courseId) {
        return lessonService.getLessonsByCourse(courseId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createLesson(@RequestBody CreateLessonRequest request) {
        // Only instructor of the course can add lessons
        if (!lessonService.isInstructorOfCourse(request.getCourseId())) {
            return ResponseEntity.status(403).body("Only the instructor can add lessons");
        }
        return ResponseEntity.ok(lessonService.createLesson(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLesson(@PathVariable Long id, @RequestBody CreateLessonRequest request) {
        if (!lessonService.isInstructorOfCourse(request.getCourseId())) {
            return ResponseEntity.status(403).body("Only the instructor can edit lessons");
        }
        return lessonService.updateLesson(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLesson(@PathVariable Long id, @RequestParam Long courseId) {
        if (!lessonService.isInstructorOfCourse(courseId)) {
            return ResponseEntity.status(403).body("Only the instructor can delete lessons");
        }
        if (lessonService.deleteLesson(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
