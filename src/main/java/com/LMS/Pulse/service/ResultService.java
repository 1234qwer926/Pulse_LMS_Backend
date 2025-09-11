package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.AssignmentAnswerDto;
import com.LMS.Pulse.Dto.CourseResultDto;
import com.LMS.Pulse.Dto.UserResultDto;
import com.LMS.Pulse.model.AssignmentAnswer;
import com.LMS.Pulse.model.Course;
import com.LMS.Pulse.model.UserSubmissions;
import com.LMS.Pulse.repository.AssignmentAnswerRepository;
import com.LMS.Pulse.repository.CourseRepository;
import com.LMS.Pulse.repository.UserSubmissionsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final CourseRepository courseRepository;
    private final UserSubmissionsRepository userSubmissionsRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;

    // This method is correct and remains unchanged.
    public List<CourseResultDto> getCourseResultsByGroup(String groupName) {
        List<Course> courses = courseRepository.findByGroupName(groupName);

        return courses.stream().map(course -> {
            List<UserSubmissions> submissions = userSubmissionsRepository.findByAssignment_Course_Id(course.getId());
            double averageScore = submissions.stream()
                    .flatMap(submission -> submission.getAnswers().stream())
                    .mapToDouble(AssignmentAnswer::getFinalScore)
                    .average()
                    .orElse(0.0);
            return new CourseResultDto(course.getId(), course.getCourseName(), averageScore);
        }).collect(Collectors.toList());
    }

    // **THE FIX** is in this method.
    public List<UserResultDto> getUserResultsForCourse(Long courseId) {
        List<UserSubmissions> submissions = userSubmissionsRepository.findByAssignment_Course_Id(courseId);

        return submissions.stream()
                .collect(Collectors.groupingBy(UserSubmissions::getUserId)) // Groups by the String userId
                .entrySet().stream()
                .map(entry -> {
                    String userId = entry.getKey(); // userId is a String
                    List<UserSubmissions> userSubmissions = entry.getValue();
                    String username = userSubmissions.get(0).getUsername();

                    double averageScore = userSubmissions.stream()
                            .flatMap(sub -> sub.getAnswers().stream())
                            .mapToDouble(AssignmentAnswer::getFinalScore)
                            .average()
                            .orElse(0.0);

                    // **FIXED**: No more Long.parseLong(). We pass the String userId directly.
                    return new UserResultDto(userId, username, averageScore);
                })
                .collect(Collectors.toList());
    }

    // The other methods in this service are correct and remain unchanged.
    @Transactional(readOnly = true)
    public List<AssignmentAnswerDto> getSubmissionsForReview(Long courseId, String userId) {
        List<UserSubmissions> submissions = userSubmissionsRepository.findByAssignment_Course_IdAndUserId(courseId, userId);
        return submissions.stream()
                .flatMap(submission -> submission.getAnswers().stream())
                .map(this::mapToAssignmentAnswerDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateFinalScore(Long answerId, double newScore) {
        AssignmentAnswer answer = assignmentAnswerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("AssignmentAnswer not found with id: " + answerId));
        answer.setFinalScore((int) newScore);
        assignmentAnswerRepository.save(answer);
    }

    private AssignmentAnswerDto mapToAssignmentAnswerDto(AssignmentAnswer answer) {
        return new AssignmentAnswerDto(
                answer.getId(),
                answer.getQuestion(),
                answer.getUserAnswer(),
                answer.getContentLink(),
                answer.getGptScore(),
                answer.getFinalScore()
        );
    } 
}
