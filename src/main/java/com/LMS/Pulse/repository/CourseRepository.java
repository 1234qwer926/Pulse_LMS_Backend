package com.LMS.Pulse.repository;

import com.LMS.Pulse.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findByCourseName(String courseName);

    @Query("SELECT DISTINCT c.courseName FROM Course c")
    List<String> findDistinctCourseNames();

    // New query to fetch courses by group
    @Query("SELECT c FROM Course c WHERE c.groupName = :group")
    List<Course> findByGroupName(@Param("group") String group);
}
