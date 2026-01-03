package com.example.healthcare.model;


import com.example.healthcare.enums.AppointmentRequestStatus;
import com.example.healthcare.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class PatientProfile {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    private String fullName;
    private String email;
    private String contactNumber;
    private int age;
    private String profileImage;

    @Enumerated(EnumType.STRING)
    private Status status;
}
