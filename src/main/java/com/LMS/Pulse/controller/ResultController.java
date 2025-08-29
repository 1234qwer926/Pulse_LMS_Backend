package com.LMS.Pulse.controller;

import com.LMS.Pulse.Dto.CourseResultDto;
import com.LMS.Pulse.Dto.UserResultDto;
import com.LMS.Pulse.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*") // Use a specific origin in production
public class ResultController {

    @Autowired
    private ResultService resultService;

    @GetMapping
    public List<CourseResultDto> getCourseResults(@RequestParam String group) {
        return resultService.getCourseResultsByGroup(group);
    }

    @GetMapping("/course/{courseId}")
    public List<UserResultDto> getUserResults(@PathVariable Long courseId) {
        return resultService.getUserResultsForCourse(courseId);
    }
}
