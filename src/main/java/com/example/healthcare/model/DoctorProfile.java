package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "DoctorProfile.withSchedules",      // Name of this graph
        attributeNodes = {
                @NamedAttributeNode("schedules")       // Fetch this relationship
        }
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorProfileId;

    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String specialization;

    @Column(nullable = true)
    private int yearsOfExperience;

    @Column(nullable = true)
    private String workingAT;

    @Column(nullable = true)
    private String contactNumber;

    private String profileImage;

    @OneToMany(
            mappedBy = "doctorProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private List<DoctorSchedule> schedules = new ArrayList<>();

    private String status;
}