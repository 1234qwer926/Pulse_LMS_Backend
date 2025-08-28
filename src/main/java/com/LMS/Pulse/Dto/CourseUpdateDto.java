package com.LMS.Pulse.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CourseUpdateDto {
    private String courseName;
    private String learningJotformName;
    private String assignmentJotformName;
    private String groupName;
    private MultipartFile imageFile;
    private MultipartFile pdfFile;
}
