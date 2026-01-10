package com.example.repository;

import com.example.model.entities.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByLesson_Course_Id(Long courseId);
}
