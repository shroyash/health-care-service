package com.example.healthcare.doctor.service;

import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.doctor.event.DoctorRegisteredEvent;
import com.example.healthcare.doctor.mapper.DoctorProfileMapper;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.doctor.dto.DoctorProfileUpdateDto;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import com.example.healthcare.common.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper mapper;

    @Transactional
    public void createDoctorProfile(DoctorRegisteredEvent event) {
        UUID userId = UUID.fromString(event.getUserId());

        if (!doctorProfileRepository.existsById(userId)) {
            DoctorProfile profile = mapper.toEntity(event);
            doctorProfileRepository.save(profile);
            log.info("Created doctor profile for userId: {}", userId);
        }
    }

    @Transactional(readOnly = true)
    public DoctorProfileResponseDto getDoctorProfile(UUID userId) {
        return mapper.toDto(findDoctorOrThrow(userId));
    }

    @Transactional
    public void updateDoctorProfile(UUID userId, DoctorProfileUpdateDto dto) {
        DoctorProfile profile = findDoctorOrThrow(userId);
        profile.setFullName(dto.getFullName());
        profile.setSpecialization(dto.getSpecialization());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setContactNumber(dto.getContactNumber());
        profile.setWorkingAT(dto.getWorkingAT());
        doctorProfileRepository.save(profile);
    }

    @Transactional(readOnly = true)
    public List<DoctorProfileResponseDto> getAllActiveDoctors() {
        return doctorProfileRepository.findByStatus(Status.ACTIVE)
                .stream()
                .map(mapper::toDto)
                .toList();
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
                    .findByYearsOfExperienceBetweenAndStatus(
                            0,
                            3,
                            Status.ACTIVE
                    );
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

    @Transactional
    public void updateProfileImage(UUID doctorId, String fileUrl) {
        DoctorProfile profile = findDoctorOrThrow(doctorId);
        profile.setProfileImage(fileUrl);
        doctorProfileRepository.save(profile);
    }

    // ── private helpers ──────────────────────────────────────────

    private DoctorProfile findDoctorOrThrow(UUID id) {
        return doctorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor profile not found"
                ));
    }
}