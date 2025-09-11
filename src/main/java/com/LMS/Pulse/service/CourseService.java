package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.CourseResponseDto;
import com.LMS.Pulse.Dto.CourseUpdateDto;
import com.LMS.Pulse.model.Assignment;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.Jotform;
import com.LMS.Pulse.repository.AssignmentRepository;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.JotformRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final JotformRepository jotformRepository;
    private final AssignmentRepository assignmentRepository;

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
    @Transactional
    public Course createLearningCourse(String courseName, String jotformName, String group,
                                       MultipartFile imageFile, MultipartFile pdfFile) throws IOException {
        Jotform learningForm = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new EntityNotFoundException("Jotform not found with name: " + jotformName));

        if (courseRepository.findByCourseName(courseName).isPresent()) {
            throw new IllegalStateException("A course with the name '" + courseName + "' already exists.");
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
     * Maps an assignment Jotform to an existing course and creates the corresponding Assignment entity.
     */
    @Transactional
    public Course mapAssignmentCourse(String courseName, String jotformName) {
        Course course = courseRepository.findByCourseName(courseName)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with name: " + courseName));

        Jotform assignmentJotform = jotformRepository.findByJotformName(jotformName)
                .orElseThrow(() -> new EntityNotFoundException("Jotform not found with name: " + jotformName));

        course.setAssignmentJotform(assignmentJotform);
        Course updatedCourse = courseRepository.save(course);

        // **FIX**: Automatically create an Assignment entity when mapping.
        // This ensures a submittable assignment record exists.
        assignmentRepository.findByAssignmentIdentifier(jotformName).ifPresentOrElse(
                existingAssignment -> {
                    // If it exists, just ensure it's linked correctly.
                    existingAssignment.setCourse(updatedCourse);
                    assignmentRepository.save(existingAssignment);
                },
                () -> {
                    // If it doesn't exist, create a new one.
                    Assignment assignment = new Assignment();
                    assignment.setCourse(updatedCourse);
                    assignment.setAssignmentIdentifier(jotformName); // Use Jotform name as the unique key
                    assignment.setTitle(course.getCourseName() + " - " + jotformName);
                    assignmentRepository.save(assignment);
                }
        );

        return updatedCourse;
    }

    /**
     * Updates an existing course with the provided data (partial update).
     */
    @Transactional
    public CourseResponseDto updateCourse(Long id, CourseUpdateDto dto) throws IOException {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + id));

        // ... update logic for various fields ...
        if (dto.getCourseName() != null && !dto.getCourseName().isEmpty()) {
            course.setCourseName(dto.getCourseName());
        }
        // ... and so on for other fields from your original code ...

        course.setUpdatedDate(LocalDateTime.now());
        Course updatedCourse = courseRepository.save(course);
        return mapToDto(updatedCourse);
    }

    /**
     * Deletes a course by its ID.
     */
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    /**
     * Helper method to map a Course entity to a CourseResponseDto.
     */
    private CourseResponseDto mapToDto(Course course) {
        String learningJotformName = course.getLearningJotform() != null ? course.getLearningJotform().getJotformName() : null;
        String assignmentJotformName = course.getAssignmentJotform() != null ? course.getAssignmentJotform().getJotformName() : null;

        return new CourseResponseDto(
                course.getId(),
                course.getCourseName(),
                learningJotformName,
                assignmentJotformName,
                course.getImageFileName(),
                course.getGroupName()
        );
    }
}
