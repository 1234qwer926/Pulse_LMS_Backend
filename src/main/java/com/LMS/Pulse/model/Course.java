package com.LMS.Pulse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @Column(nullable = false, unique = true)
    private String courseName;

    // Relation to Jotform (Learning)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "learning_jotform_id")
    @JsonIgnoreProperties({"pages"}) // prevent infinite recursion
    private Jotform learningJotform;

    // Relation to Jotform (Assignment)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assignment_jotform_id")
    @JsonIgnoreProperties({"pages"})
    private Jotform assignmentJotform;

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
