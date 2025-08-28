package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.CourseResponseDto;
import com.LMS.Pulse.Dto.CourseUpdateDto;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.JotformRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JotformRepository jotformRepository;

    /**
     * Fetches all courses belonging to a specific group.
     */
    public List<CourseResponseDto> getCoursesByGroup(String group) {
        List<Course> courses = courseRepository.findByGroupName(group);
        return courses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Fetches a list of all distinct course names.
     */
    public List<String> getCourseNames() {
        return courseRepository.findDistinctCourseNames();
    }

    /**
     * Creates a new course with a learning Jotform and optional files.
     */
    public Course createLearningCourse(String courseName, String jotformName, String group,
                                       MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
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

    /**
     * Maps an assignment Jotform to an existing course.
     */
    public Course mapAssignmentCourse(String courseName, String jotformName, String group) {
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseName));

        Jotform assignmentForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new RuntimeException("Jotform not found: " + jotformName));

        course.setAssignmentJotform(assignmentForm);
        course.setGroupName(group);
        course.setUpdatedDate(LocalDateTime.now());

        return courseRepository.save(course);
    }

    /**
     * Updates an existing course with the provided data (partial update).
     */
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseUpdateDto dto) throws IOException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        // Update course name if provided
        if (dto.getCourseName() != null && !dto.getCourseName().isEmpty()) {
            course.setCourseName(dto.getCourseName());
        }

        // Update group name if provided
        if (dto.getGroupName() != null && !dto.getGroupName().isEmpty()) {
            course.setGroupName(dto.getGroupName());
        }

        // Update learning Jotform if provided
        if (dto.getLearningJotformName() != null && !dto.getLearningJotformName().isEmpty()) {
            Jotform learningJotform = jotformRepository.findByJotformName(dto.getLearningJotformName())
                    .orElseThrow(() -> new RuntimeException("Learning Jotform not found: " + dto.getLearningJotformName()));
            course.setLearningJotform(learningJotform);
        }

        // Update assignment Jotform if provided
        if (dto.getAssignmentJotformName() != null && !dto.getAssignmentJotformName().isEmpty()) {
            Jotform assignmentJotform = jotformRepository.findByJotformName(dto.getAssignmentJotformName())
                    .orElseThrow(() -> new RuntimeException("Assignment Jotform not found: " + dto.getAssignmentJotformName()));
            course.setAssignmentJotform(assignmentJotform);
        } else {
            course.setAssignmentJotform(null);
        }

        // Update image file if provided
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            course.setImageFile(dto.getImageFile().getBytes());
            course.setImageFileName(dto.getImageFile().getOriginalFilename());
        }

        // Update PDF file if provided
        if (dto.getPdfFile() != null && !dto.getPdfFile().isEmpty()) {
            course.setPdfFile(dto.getPdfFile().getBytes());
            course.setPdfFileName(dto.getPdfFile().getOriginalFilename());
        }

        course.setUpdatedDate(LocalDateTime.now());
        Course updatedCourse = courseRepository.save(course);
        return mapToDto(updatedCourse);
    }

    /**
     * Deletes a course by its ID.
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    /**
     * Helper method to map a Course entity to a CourseResponseDto.
     * This now correctly includes the course ID.
     */
    private CourseResponseDto mapToDto(Course course) {
        String learningJotformName = course.getLearningJotform() != null ? course.getLearningJotform().getJotformName() : null;
        String assignmentJotformName = course.getAssignmentJotform() != null ? course.getAssignmentJotform().getJotformName() : null;

        return new CourseResponseDto(
                course.getId(), // This is the corrected line
                course.getCourseName(),
                learningJotformName,
                assignmentJotformName,
                course.getImageFileName(),
                course.getGroupName()
        );
    }
}
