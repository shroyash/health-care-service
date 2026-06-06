package com.example.healthcare.patient.model;

import com.example.healthcare.common.model.BaseEntity;
import com.example.healthcare.common.enums.Status;
import com.example.healthcare.common.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "patient_profiles")
public class PatientProfile extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String contactNumber;
    private String profileImage;
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String country;

    @Enumerated(EnumType.STRING)
    private Status status;

    // ✅ equals/hashCode based on id only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PatientProfile)) return false;
        PatientProfile that = (PatientProfile) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}