package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:5173")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Create new course with learning mapping
    @PostMapping("/learning")
    public ResponseEntity<?> createCourseWithLearning(
            @RequestParam("courseName") String courseName,
            @RequestParam("jotformName") String jotformName,
            @RequestParam("group") String group,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "pdfFile", required = false) MultipartFile pdfFile) {
        try {
            Course course = new Course();
            course.setCourseName(courseName);
            course.setLearningJotformName(jotformName);
            course.setGroupName(group);

            if (imageFile != null && !imageFile.isEmpty()) {
                course.setImageFile(imageFile.getBytes());
                course.setImageFileName(imageFile.getOriginalFilename());
            }

            if (pdfFile != null && !pdfFile.isEmpty()) {
                course.setPdfFile(pdfFile.getBytes());
                course.setPdfFileName(pdfFile.getOriginalFilename());
            }

            Course saved = courseService.createCourseWithLearning(course);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save files");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Attach assignment to existing course
    @PostMapping("/assignment")
    public ResponseEntity<?> attachAssignment(@RequestBody Map<String, String> payload) {
        try {
            String courseName = payload.get("courseName");
            String jotformName = payload.get("jotformName");
            String group = payload.get("group");

            // Basic validation
            if (courseName == null || jotformName == null || group == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing required fields: courseName, jotformName, or group");
            }

            Course updated = courseService.attachAssignmentToCourse(courseName, jotformName, group);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Fetch distinct course names
    @GetMapping("/names")
    public ResponseEntity<List<String>> getDistinctCourseNames() {
        List<String> courses = courseService.getDistinctCourseNames();
        return ResponseEntity.ok(courses);
    }

    // New endpoint to fetch courses by group
    @GetMapping
    public ResponseEntity<List<Course>> getCoursesByGroup(@RequestParam("group") String group) {
        List<Course> courses = courseService.getCoursesByGroup(group);
        if (courses.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(courses);
    }
}
