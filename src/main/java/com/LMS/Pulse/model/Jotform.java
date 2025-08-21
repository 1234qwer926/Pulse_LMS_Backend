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
@Table(name = "jotforms")
public class Jotform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String jotformName;

    private Integer totalPages;

    @OneToMany(mappedBy = "jotform", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JotformPage> pages = new ArrayList<>();
}
