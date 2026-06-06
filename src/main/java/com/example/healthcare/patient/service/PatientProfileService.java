package com.example.healthcare.patient.service;


import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.patient.dto.PatientProfileDTO;
import com.example.healthcare.patient.dto.PatientProfileUpdateDto;
import com.example.healthcare.patient.event.UserRegisteredEvent;
import com.example.healthcare.patient.mapper.PatientProfileMapper;
import com.example.healthcare.patient.model.PatientProfile;
import com.example.healthcare.patient.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper mapper;

    @Transactional
    public void createPatientProfile(UserRegisteredEvent event) {
        UUID userId = UUID.fromString(event.getUserId());

        if (!patientProfileRepository.existsById(userId)) {
            PatientProfile profile = mapper.toEntity(event);
            patientProfileRepository.save(profile);
            log.info("Created patient profile for userId: {}", userId);
        }
    }

    @Transactional(readOnly = true)
    public PatientProfileDTO getPatientProfile(UUID userId) {
        return mapper.toDto(findPatientOrThrow(userId));
    }

    @Transactional
    public void updatePatientProfile(UUID userId, PatientProfileUpdateDto dto) {
        PatientProfile profile = findPatientOrThrow(userId);
        profile.setFullName(dto.getFullname());
        profile.setContactNumber(dto.getContactNumber());
        patientProfileRepository.save(profile);
    }

    @Transactional
    public void updateProfileImage(UUID userId, String fileUrl) {
        PatientProfile profile = findPatientOrThrow(userId);
        profile.setProfileImage(fileUrl);
        patientProfileRepository.save(profile);
    }

    // ── private helpers ──────────────────────────────────────────

    private PatientProfile findPatientOrThrow(UUID id) {
        return patientProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }
}