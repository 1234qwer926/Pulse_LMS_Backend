package com.LMS.Pulse.controller;

import com.LMS.Pulse.model.AssignmentAnswer;
import com.LMS.Pulse.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assignment")
@CrossOrigin(origins = "*") // Use a specific origin in production
public class AssignmentController {

    @Autowired
    private AssignmentService assignmentService;

    @GetMapping("/{courseId}/{userId}")
    public List<AssignmentAnswer> getAssignmentDetails(@PathVariable Long courseId, @PathVariable Long userId) {
        return assignmentService.getAssignmentDetails(courseId, userId);
    }

    @PutMapping("/score/{answerId}")
    public AssignmentAnswer updateScore(@PathVariable Long answerId, @RequestBody Map<String, Integer> payload) {
        Integer finalScore = payload.get("finalScore");
        if (finalScore == null) {
            throw new IllegalArgumentException("finalScore is required.");
        }
        return assignmentService.updateFinalScore(answerId, finalScore);
    }
}
