package com.LMS.Pulse.service;

import com.LMS.Pulse.Dto.AnswerSubmissionDto;
import com.LMS.Pulse.Dto.UserSubmissionDto;
import com.LMS.Pulse.model.*;
import com.LMS.Pulse.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final UserSubmissionsRepository userSubmissionsRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentAnswerRepository assignmentAnswerRepository;
    private final S3Service s3Service;
    private final GeminiService geminiService;

    /**
     * Processes a full user submission, including JSON data, an identity photo, and video answers.
     * It uses a string-based identifier to find the assignment, preventing NumberFormatException.
     */
    @Transactional
    public UserSubmissions processSubmission(UserSubmissionDto dto, MultipartFile identityPhoto, List<MultipartFile> videoFiles) throws IOException {

        // **THE FIX**: Instead of parsing an ID, we find the assignment by its unique string identifier.
        // This is the key change that resolves the error.
        String assignmentIdentifier = dto.getJotformId();
        Assignment assignment = assignmentRepository.findByAssignmentIdentifier(assignmentIdentifier)
                .orElseThrow(() -> new EntityNotFoundException("No assignment found with identifier: " + assignmentIdentifier + ". Please ensure the assignment is mapped to the course first."));

        // 1. Upload the identity photo to S3
        String photoKey = s3Service.uploadFile(identityPhoto);
        String photoUrl = s3Service.generatePresignedUrl(photoKey);

        // 2. Create the main UserSubmissions record and link it to the found assignment
        UserSubmissions submission = new UserSubmissions();
        submission.setUsername(dto.getUsername());
        submission.setUserId(dto.getUserId());
        submission.setAssignment(assignment);
        submission.setUserPhotoUrl(photoUrl);
        submission.setAnswers(new ArrayList<>());

        // Save the submission now to get its ID for linking answers
        UserSubmissions savedSubmission = userSubmissionsRepository.save(submission);

        // 3. Process each answer: upload video, get AI score, and save the record
        for (int i = 0; i < dto.getAnswers().size(); i++) {
            AnswerSubmissionDto answerDto = dto.getAnswers().get(i);
            MultipartFile videoFile = videoFiles.get(i);

            // Upload video and get its presigned URL
            String videoKey = s3Service.uploadFile(videoFile);
            String videoUrl = s3Service.generatePresignedUrl(videoKey);

            // Evaluate the transcript with Gemini to get a score
            int score = geminiService.getScoreForAnswer(answerDto.getQuestionText(), answerDto.getTranscript());

            // Create and save the AssignmentAnswer entity
            AssignmentAnswer answer = new AssignmentAnswer();
            answer.setUserSubmission(savedSubmission); // Link to the parent submission
            answer.setQuestion(answerDto.getQuestionText());
            answer.setUserAnswer(answerDto.getTranscript());
            answer.setContentLink(videoUrl);
            answer.setGptScore(score);
            answer.setFinalScore(score); // Initially, final score is the same as the GPT score

            // Add the saved answer to the submission's list
            savedSubmission.getAnswers().add(assignmentAnswerRepository.save(answer));
        }

        return savedSubmission;
    }
}
