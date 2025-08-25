package com.LMS.Pulse.controller;

import com.LMS.Pulse.Dto.CourseResponseDto;
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
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:5173")
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
        // ... (existing code is unchanged)
        List<String> courseNames = courseService.getCourseNames();
        return ResponseEntity.ok(courseNames);
    }

    @PostMapping("/learning")
    public ResponseEntity<Course> mapLearningJotform(
            // ... (existing code is unchanged)
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
            // ... (existing code is unchanged)
            @RequestBody MapAssignmentRequest request
    ) {
        Course updatedCourse = courseService.mapAssignmentCourse(
                request.getCourseName(),
                request.getJotformName(),
                request.getGroup()
        );
        return ResponseEntity.ok(updatedCourse);
    }
}
