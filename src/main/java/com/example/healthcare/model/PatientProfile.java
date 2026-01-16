    package com.example.healthcare.model;


    import com.example.healthcare.enums.Gender;
    import com.example.healthcare.enums.Status;
    import jakarta.persistence.*;
    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.time.LocalDate;
    import java.util.UUID;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Entity
    public class PatientProfile {

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
    }
