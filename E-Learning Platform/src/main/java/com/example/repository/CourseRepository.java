package com.example.repository;

import com.example.model.entities.Course;
import com.example.model.entities.CourseStatus;
import com.example.model.entities.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByStatus(CourseStatus status);
    List<Course> findByCategory_Name(String category);
    List<Course> findByDifficulty(DifficultyLevel level);
    List<Course> findByInstructor_Id(Long instructorId);
}
