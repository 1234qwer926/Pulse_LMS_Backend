package com.LMS.Pulse.controller;

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
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Get all courses
    @GetMapping("/getall")
    public List<Course> getAllCourses() {
        return courseService.getAll();
    }

    // Map Learning Jotform to a course
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

    // Map Assignment Jotform to an existing course
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
}