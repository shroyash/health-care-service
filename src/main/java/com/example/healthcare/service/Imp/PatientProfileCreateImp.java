package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.event.UserRegisteredEvent;
import com.example.healthcare.enums.Status;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.PatientProfile;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.PatientProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PatientProfileCreateImp implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;


    public void createPatientProfile(UserRegisteredEvent event) {

        UUID userId = UUID.fromString(event.getUserId());
        boolean exists = patientProfileRepository.findById(userId).isPresent();
        if (!exists) {
            PatientProfile profile = PatientProfile.builder()
                    .id(userId)
                    .fullName(event.getUsername())
                    .email(event.getEmail())
                    .contactNumber(null)
                    .status(Status.ACTIVE)
                    .build();
            log.info("Created patient profile: {}", profile);

            patientProfileRepository.save(profile);
        }
    }

    @Override
    public PatientProfileDTO getPatientProfile(UUID userId) {
        PatientProfile patientProfile = patientProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));

        return mapToDto(patientProfile);
    }

    @Override
    public void updatePatientProfile(UUID userId, PatientProfileUpdateDto dto) {
        PatientProfile profile = patientProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        profile.setFullName(dto.getFullname());
        profile.setContactNumber(dto.getContactNumber());

        patientProfileRepository.save(profile);
    }

    @Override
    public List<PatientProfileDTO> getAllPatients() {
        List<PatientProfile> patients = patientProfileRepository.findAll();
        return patients.stream()
                .map(patient -> PatientProfileDTO.builder()
                        .patientId(patient.getId())
                        .fullName(patient.getFullName())
                        .email(patient.getEmail())
                        .contactNumber(patient.getContactNumber())
                        .build()
                )
                .collect(Collectors.toList());
    }


    // Service layer
    public PatientProfileDTO suspendPatient(UUID patientId) {
        PatientProfile profile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        profile.setStatus(Status.INACTIVE); // mark as suspended
        patientProfileRepository.save(profile);
        log.info("Suspended patient account: {}", patientId);
        return mapToDto(profile); // convert entity to DTO
    }

    public PatientProfileDTO restorePatient(UUID patientId) {
        PatientProfile profile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        profile.setStatus(Status.ACTIVE);
        patientProfileRepository.save(profile);
        log.info("Restored patient account: {}", patientId);
        return mapToDto(profile); // convert entity to DTO
    }

    @Override
    public void updateProfileImage(UUID userId, String fileUrl) {
        PatientProfile patientProfile = patientProfileRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        patientProfile.setProfileImage(fileUrl);
        patientProfileRepository.save(patientProfile);
    }


    // Helper method to map entity to DTO
    private PatientProfileDTO mapToDto(PatientProfile profile) {
        return PatientProfileDTO.builder()
                .patientId(profile.getId())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .contactNumber(profile.getContactNumber())
                .status(profile.getStatus().name())
                .profileImgUrl(profile.getProfileImage())
                .build();
    }



}
