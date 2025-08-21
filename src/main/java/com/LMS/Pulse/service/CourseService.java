package com.LMS.Pulse.service;

import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    // Create new course with learning mapping
    public Course createCourseWithLearning(Course course) {
        Optional<Course> existing = courseRepository.findByCourseName(course.getCourseName());
        if (existing.isPresent()) {
            throw new RuntimeException("Course name already exists");
        }
        return courseRepository.save(course);
    }

    // Attach assignment to existing course
    public Course attachAssignmentToCourse(String courseName, String jotformName, String group) {
        Optional<Course> existingCourse = courseRepository.findByCourseName(courseName);
        if (existingCourse.isEmpty()) {
            throw new RuntimeException("Course not found");
        }

        Course course = existingCourse.get();
        course.setAssignmentJotformName(jotformName);
        course.setGroupName(group); // Update group if needed
        return courseRepository.save(course);
    }

    public List<String> getDistinctCourseNames() {
        return courseRepository.findDistinctCourseNames();
    }

    // New method to fetch courses by group
    public List<Course> getCoursesByGroup(String group) {
        return courseRepository.findByGroupName(group);
    }
}
