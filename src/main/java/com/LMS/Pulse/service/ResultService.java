package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.CourseResultDto;
import com.LMS.Pulse.Dto.UserResultDto;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.Assignment;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.AssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResultService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    public List<CourseResultDto> getCourseResultsByGroup(String groupName) {
        List<Course> courses = courseRepository.findByGroupName(groupName);
        return courses.stream().map(course -> {
            List<Assignment> assignments = assignmentRepository.findByCourseId(course.getId());
            double averageScore = assignments.stream()
                    .mapToDouble(Assignment::getTotalScore)
                    .average()
                    .orElse(0.0);
            return new CourseResultDto(course.getId(), course.getCourseName(), averageScore);
        }).collect(Collectors.toList());
    }

    public List<UserResultDto> getUserResultsForCourse(Long courseId) {
        return assignmentRepository.findByCourseId(courseId).stream()
                .map(assignment -> new UserResultDto(
                        assignment.getUser().getId(),
                        assignment.getUser().getUsername(),
                        assignment.getTotalScore()
                ))
                .collect(Collectors.toList());
    }
}
