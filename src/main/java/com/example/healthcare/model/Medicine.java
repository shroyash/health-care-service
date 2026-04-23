package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "medicines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String dosage;
    private String category;

    @Column(columnDefinition = "TEXT")
    private String sideEffects;

    private String manufacturer;

    @Column(nullable = false)
    private UUID addedByDoctorId;
}