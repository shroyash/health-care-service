package com.example.healthcare.doctor.service;


import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.doctor.mapper.DoctorProfileMapper;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import com.example.healthcare.doctor.dto.DoctorWithScheduleDto;
import com.example.healthcare.common.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientDoctorService {
    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper mapper;

    @Transactional(readOnly = true)
    public List<DoctorWithScheduleDto> getAllAvailableDoctors() {
        LocalDate today = LocalDate.now();
        return doctorProfileRepository.findAll().stream()
                .map(doctor -> DoctorWithScheduleDto.builder()
                        .doctorProfileId(doctor.getId())
                        .name(doctor.getFullName())
                        .profileUrl(doctor.getProfileImage())
                        .specialty(doctor.getSpecialization())
                        .email(doctor.getEmail())
                        .phone(doctor.getContactNumber())
                        .schedules(
                                doctor.getSchedules().stream()
                                        .filter(s -> !s.getScheduleDate().isBefore(today))
                                        .map(schedule -> DoctorWithScheduleDto.ScheduleDto.builder()
                                                .date(schedule.getScheduleDate().toString())
                                                .startTime(schedule.getStartTime().toString())
                                                .endTime(schedule.getEndTime().toString())
                                                .available(schedule.isAvailable())
                                                .build())
                                        .collect(Collectors.toList())
                        )
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DoctorProfileResponseDto> getSpecializedDoctorProfile(String specialization) {
        List<DoctorProfile> doctors = doctorProfileRepository
                .findBySpecializationAndStatus(specialization, Status.ACTIVE);
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No doctors found with specialization: " + specialization
            );
        }
        return doctors.stream().map(mapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<DoctorProfileResponseDto> getDoctorsByExperience(String level) {
        List<DoctorProfile> doctors = switch (level.toLowerCase()) {
            case "junior" -> doctorProfileRepository
                    .findByYearsOfExperienceBetweenAndStatus(0, 3, Status.ACTIVE); // ✅ fixed
            case "mid", "mid-level" -> doctorProfileRepository
                    .findByYearsOfExperienceBetweenAndStatus(4, 7, Status.ACTIVE);
            case "experienced" -> doctorProfileRepository
                    .findByYearsOfExperienceGreaterThanEqualAndStatus(8, Status.ACTIVE);
            default -> throw new IllegalArgumentException(
                    "Invalid experience level: " + level
            );
        };
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No doctors found for experience level: " + level
            );
        }
        return doctors.stream().map(mapper::toDto).toList();
    }
}