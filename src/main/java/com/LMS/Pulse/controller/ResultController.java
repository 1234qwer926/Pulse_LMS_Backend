package com.LMS.Pulse.controller;

import com.LMS.Pulse.Dto.AssignmentAnswerDto;
import com.LMS.Pulse.Dto.CourseResultDto;
import com.LMS.Pulse.Dto.UpdateScoreDto;
import com.LMS.Pulse.Dto.UserResultDto;
import com.LMS.Pulse.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    // GET /api/results?group={groupName}
    @GetMapping
    public List<CourseResultDto> getCourseResults(@RequestParam String group) {
        return resultService.getCourseResultsByGroup(group);
    }

    // GET /api/results/course/{courseId}
    @GetMapping("/course/{courseId}")
    public List<UserResultDto> getUserResults(@PathVariable Long courseId) {
        return resultService.getUserResultsForCourse(courseId);
    }

    // **NEW ENDPOINT** for AssignmentReview component
    // GET /api/results/submissions/{courseId}/{userId}
    @GetMapping("/submissions/{courseId}/{userId}")
    public ResponseEntity<List<AssignmentAnswerDto>> getSubmissionsForReview(
            @PathVariable Long courseId,
            @PathVariable String userId) {
        List<AssignmentAnswerDto> submissionDetails = resultService.getSubmissionsForReview(courseId, userId);
        return ResponseEntity.ok(submissionDetails);
    }

    // **NEW ENDPOINT** for saving an updated score
    // PUT /api/results/answer/{answerId}
    @PutMapping("/answer/{answerId}")
    public ResponseEntity<Void> updateFinalScore(
            @PathVariable Long answerId,
            @RequestBody UpdateScoreDto updateScoreDto) {
        resultService.updateFinalScore(answerId, updateScoreDto.getFinalScore());
        return ResponseEntity.ok().build();
    }
}
