package com.example.healthcare.doctor.mapper;

import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import com.example.healthcare.common.enums.Status;
import com.example.healthcare.doctor.event.DoctorRegisteredEvent;
import com.example.healthcare.doctor.model.DoctorProfile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class DoctorProfileMapper {

    public DoctorProfileResponseDto toDto(DoctorProfile doctor) {
        return DoctorProfileResponseDto.builder()
                .doctorProfileId(doctor.getId())
                .profileImgUrl(doctor.getProfileImage())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .workingAT(doctor.getWorkingAT())
                .contactNumber(doctor.getContactNumber())
                .dateOfBirth(doctor.getDateOfBirth() != null
                        ? doctor.getDateOfBirth().toString() : null)
                .gender(doctor.getGender() != null
                        ? doctor.getGender().name() : null)
                .country(doctor.getCountry())
                .status(doctor.getStatus() != null
                        ? doctor.getStatus().name() : null)
                .build();
    }

    public DoctorProfile toEntity(DoctorRegisteredEvent event) {
        return DoctorProfile.builder()
                .id(UUID.fromString(event.getUserId()))
                .fullName(event.getUsername())
                .email(event.getEmail())
                .specialization("Generalist")
                .yearsOfExperience(0)
                .contactNumber(null)
                .gender(event.getGender())
                .country(event.getCountry())
                .dateOfBirth(LocalDate.parse(event.getDateOfBirth()))
                .status(Status.ACTIVE)
                .build();
    }
}