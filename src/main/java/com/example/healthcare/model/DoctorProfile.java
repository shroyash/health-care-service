package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String fullName;
    private String email;
    private String specialization;
    private int yearsOfExperience;
    private String workingAT;
    private String contactNumber;

    @OneToMany(mappedBy = "doctorProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DoctorSchedule> schedules;

}
