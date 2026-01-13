package com.example.healthcare.model;

import com.example.healthcare.enums.Gender;
import com.example.healthcare.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = true)
    private String specialization;

    @Column(nullable = true)
    private int yearsOfExperience;

    private String workingAT;

    private String contactNumber;


    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    private String country;

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

    @Enumerated(EnumType.STRING)
    private Status status;
}