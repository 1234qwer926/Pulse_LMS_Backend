package com.LMS.Pulse.repository;

import com.LMS.Pulse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseName(String courseName);

    @Query("SELECT DISTINCT c.courseName FROM Course c ORDER BY c.courseName")
    List<String> findDistinctCourseNames();

    // New method to find courses by group name
    List<Course> findByGroupName(String groupName);
}
