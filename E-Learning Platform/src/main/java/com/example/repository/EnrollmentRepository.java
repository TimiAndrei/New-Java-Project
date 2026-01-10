package com.example.repository;

import com.example.model.entities.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent_Id(Long studentId);
    Optional<Enrollment> findByStudent_IdAndCourse_Id(Long studentId, Long courseId);
}
