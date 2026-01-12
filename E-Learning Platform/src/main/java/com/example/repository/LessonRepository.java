package com.example.repository;

import com.example.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
	java.util.List<Lesson> findByCourse_Id(Long courseId);
}
