package com.LMS.Pulse.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "jotform_pages")
public class JotformPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer page;

    private Integer totalElements;

    @ManyToOne
    @JoinColumn(name = "jotform_id", nullable = false)
    private Jotform jotform;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JotformElement> elements = new ArrayList<>();
}
