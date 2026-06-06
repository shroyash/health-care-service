package com.example.healthcare.doctor.model;

import com.example.healthcare.common.model.BaseEntity;
import com.example.healthcare.common.enums.Gender;
import com.example.healthcare.common.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NamedEntityGraph(
        name = "DoctorProfile.withSchedules",
        attributeNodes = {
                @NamedAttributeNode("schedules")
        }
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "schedules")
@Table(name = "doctor_profiles")
public class DoctorProfile extends BaseEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String email;

    private String specialization;
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
    @Builder.Default
    private List<DoctorSchedule> schedules = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Status status;

    // ✅ equals/hashCode based on id only
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DoctorProfile)) return false;
        DoctorProfile that = (DoctorProfile) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}