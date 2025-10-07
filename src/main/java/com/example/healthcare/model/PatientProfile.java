package com.example.healthcare.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long doctorProfileId;

    private Long userId;
    private String fullName;
    private String email;
    private String contactNumber;
}
