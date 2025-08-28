package com.LMS.Pulse.controller;

import com.LMS.Pulse.Dto.CourseResponseDto;
import com.LMS.Pulse.Dto.CourseUpdateDto;
import com.LMS.Pulse.Dto.MapAssignmentRequest;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "*") // Fine for testing without security
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Endpoint to get courses, filtered by group
    @GetMapping
    public ResponseEntity<List<CourseResponseDto>> getCoursesByGroup(@RequestParam("group") String group) {
        List<CourseResponseDto> courses = courseService.getCoursesByGroup(group);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/names")
    public ResponseEntity<List<String>> getCourseNames() {
        List<String> courseNames = courseService.getCourseNames();
        return ResponseEntity.ok(courseNames);
    }

    @PostMapping("/learning")
    public ResponseEntity<Course> mapLearningJotform(
            @RequestParam("courseName") String courseName,
            @RequestParam("jotformName") String jotformName,
            @RequestParam("group") String group,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile
    ) throws IOException {
        Course course = courseService.createLearningCourse(courseName, jotformName, group, imageFile, pdfFile);
        return ResponseEntity.ok(course);
    }

    @PostMapping("/assignment")
    public ResponseEntity<Course> mapAssignmentJotform(
            @RequestBody MapAssignmentRequest request
    ) {
        Course updatedCourse = courseService.mapAssignmentCourse(
                request.getCourseName(),
                request.getJotformName(),
                request.getGroup()
        );
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Endpoint to update an existing course.
     * It uses @ModelAttribute to handle both JSON fields and file uploads.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDto> updateCourse(
            @PathVariable Long id,
            @ModelAttribute CourseUpdateDto courseUpdateDto
    ) throws IOException {
        CourseResponseDto updatedCourse = courseService.updateCourse(id, courseUpdateDto);
        return ResponseEntity.ok(updatedCourse);
    }

    /**
     * Endpoint to delete a course by its ID.
     */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
            courseService.deleteCourse(id);
            return ResponseEntity.noContent().build();
        }
}
