package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.CourseResponseDto;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.JotformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JotformRepository jotformRepository;

    // New method to fetch courses by group and map to DTO
    public List<CourseResponseDto> getCoursesByGroup(String group) {
        List<Course> courses = courseRepository.findByGroupName(group);
        return courses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Helper method to map a Course entity to a DTO
    private CourseResponseDto mapToDto(Course course) {
        String learningJotformName = course.getLearningJotform() != null ? course.getLearningJotform().getJotformName() : null;
        String assignmentJotformName = course.getAssignmentJotform() != null ? course.getAssignmentJotform().getJotformName() : null;

        return new CourseResponseDto(
                course.getCourseName(),
                learningJotformName,
                assignmentJotformName,
                course.getImageFileName(),
                course.getGroupName()
        );
    }

    public List<String> getCourseNames() {
        return courseRepository.findDistinctCourseNames();
    }

    public Course createLearningCourse(String courseName, String jotformName, String group,
                                       MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        // ... (existing code is unchanged)
        Jotform learningForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new RuntimeException("Jotform not found: " + jotformName));

        if (courseRepository.findByCourseName(courseName).isPresent()) {
            throw new RuntimeException("A course with the name '" + courseName + "' already exists.");
        }

        Course course = new Course();
        course.setCourseName(courseName);
        course.setLearningJotform(learningForm);
        course.setGroupName(group);

        if (imageFile != null && !imageFile.isEmpty()) {
            course.setImageFile(imageFile.getBytes());
            course.setImageFileName(imageFile.getOriginalFilename());
        }
        if (pdfFile != null && !pdfFile.isEmpty()) {
            course.setPdfFile(pdfFile.getBytes());
            course.setPdfFileName(pdfFile.getOriginalFilename());
        }

        return courseRepository.save(course);
    }

    public Course mapAssignmentCourse(String courseName, String jotformName, String group) {
        // ... (existing code is unchanged)
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseName));

        Jotform assignmentForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new RuntimeException("Jotform not found: " + jotformName));

        course.setAssignmentJotform(assignmentForm);
        course.setGroupName(group);

        return courseRepository.save(course);
    }
}
