package com.example.healthcare.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorProfileId;

    private Long doctorId;
    private String fullName;
    private String email;
    private String specialization;
    private int yearsOfExperience;
    private String workingAT;
    private String contactNumber;

}
