package com.example.healthcare.mapper;

import com.example.healthcare.dto.response.PatientProfileDTO;
import com.example.healthcare.enums.Status;
import com.example.healthcare.event.UserRegisteredEvent;
import com.example.healthcare.model.PatientProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class PatientProfileMapper {

    public PatientProfileDTO toDto(PatientProfile patient) {
        return PatientProfileDTO.builder()
                .patientId(patient.getId())
                .profileImgUrl(patient.getProfileImage())
                .fullName(patient.getFullName())
                .email(patient.getEmail())
                .contactNumber(patient.getContactNumber())
                .dateOfBirth(patient.getDateOfBirth() != null
                        ? patient.getDateOfBirth().toString() : null)
                .gender(patient.getGender() != null
                        ? patient.getGender().name() : null)
                .country(patient.getCountry())
                .status(patient.getStatus() != null
                        ? patient.getStatus().name() : null)
                .build();
    }

    public PatientProfile toEntity(UserRegisteredEvent event) {
        return PatientProfile.builder()
                .id(UUID.fromString(event.getUserId()))
                .fullName(event.getUsername())
                .email(event.getEmail())
                .contactNumber(null)
                .country(event.getCountry())
                .dateOfBirth(LocalDate.parse(event.getDateOfBirth()))
                .gender(event.getGender())
                .status(Status.ACTIVE)
                .build();
    }
}