package com.LMS.Pulse.repository;

import com.LMS.Pulse.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseId(Long courseId);
    Optional<Assignment> findByCourseIdAndUserId(Long courseId, Long userId);
}
