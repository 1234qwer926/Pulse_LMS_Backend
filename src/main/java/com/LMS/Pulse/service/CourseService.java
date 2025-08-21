package com.LMS.Pulse.service;

import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.JotformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JotformRepository jotformRepository;

    public List<Course> getAll() {
        return courseRepository.findAll();
    }

    // Create new course with learning Jotform
    public Course createLearningCourse(String courseName, String jotformName, String group,
                                       MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        Jotform learningForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new RuntimeException("Jotform not found: " + jotformName));

        Course course = new Course();
        course.setCourseName(courseName);
        course.setLearningJotform(learningForm);
        course.setGroupName(group);

        if (imageFile != null) {
            course.setImageFile(imageFile.getBytes());
            course.setImageFileName(imageFile.getOriginalFilename());
        }
        if (pdfFile != null) {
            course.setPdfFile(pdfFile.getBytes());
            course.setPdfFileName(pdfFile.getOriginalFilename());
        }

        return courseRepository.save(course);
    }

    // Update existing course with Assignment Jotform
    public Course mapAssignmentCourse(String courseName, String jotformName, String group) {
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseName));

        Jotform assignmentForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new RuntimeException("Jotform not found: " + jotformName));

        course.setAssignmentJotform(assignmentForm);
        course.setGroupName(group);

        return courseRepository.save(course);
    }
}
