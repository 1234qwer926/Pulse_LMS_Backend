package com.LMS.Pulse.service;

import com.LMS.Pulse.model.Assignment;
import com.LMS.Pulse.model.AssignmentAnswer;
import com.LMS.Pulse.repository.AssignmentRepository;
import com.LMS.Pulse.repository.AssignmentAnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private AssignmentAnswerRepository answerRepository;

    public List<AssignmentAnswer> getAssignmentDetails(Long courseId, Long userId) {
        return assignmentRepository.findByCourseIdAndUserId(courseId, userId)
                .map(Assignment::getAnswers)
                .orElse(List.of());
    }

    @Transactional
    public AssignmentAnswer updateFinalScore(Long answerId, int finalScore) {
        AssignmentAnswer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new RuntimeException("Answer not found with id: " + answerId));

        answer.setFinalScore(finalScore);
        AssignmentAnswer updatedAnswer = answerRepository.save(answer);

        // Recalculate total score for the assignment
        Assignment assignment = assignmentRepository.findAll().stream()
                .filter(a -> a.getAnswers().stream().anyMatch(ans -> ans.getId().equals(answerId)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Assignment not found for the given answer"));

        double newTotalScore = assignment.getAnswers().stream()
                .mapToDouble(AssignmentAnswer::getFinalScore)
                .sum();

        assignment.setTotalScore(newTotalScore);
        assignmentRepository.save(assignment);

        return updatedAnswer;
    }
}
