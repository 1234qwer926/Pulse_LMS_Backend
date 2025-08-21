package com.LMS.Pulse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // Ensure course names are unique
    private String courseName;

    @Column
    private String learningJotformName; // Mapped learning Jotform

    @Column
    private String assignmentJotformName; // Attached assignment Jotform (can be updated later)

    @Column(nullable = false)
    private String groupName;

    @Lob
    private byte[] imageFile;

    private String imageFileName;

    @Lob
    private byte[] pdfFile;

    private String pdfFileName;

    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updatedDate = LocalDateTime.now();
}
