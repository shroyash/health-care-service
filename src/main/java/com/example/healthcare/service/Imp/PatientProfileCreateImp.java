package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.PatientProfile;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.PatientProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class PatientProfileCreateImp implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    public void createPatientProfile(String token) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        String userName = JwtUtils.extractUserNameFromToken(token);
        String email = JwtUtils.extractEmailFromToken(token);

        boolean exists = patientProfileRepository.findByUserId(userId).isPresent();
        if (!exists) {
            PatientProfile profile = PatientProfile.builder()
                    .userId(userId)
                    .fullName(userName)
                    .email(email)
                    .contactNumber(null)
                    .status("active")
                    .build();
            log.info("Created patient profile: {}", profile);

            patientProfileRepository.save(profile);
        }
    }

    @Override
    public void updatePatientProfile(Long userId, PatientProfileUpdateDto dto) {
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
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
                        .patientId(patient.getPatientProfileId())
                        .fullName(patient.getFullName())
                        .email(patient.getEmail())
                        .contactNumber(patient.getContactNumber())
                        .build()
                )
                .collect(Collectors.toList());
    }


    // Service layer
    public PatientProfileDTO suspendPatient(Long patientId) {
        PatientProfile profile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        profile.setStatus("suspended"); // mark as suspended
        patientProfileRepository.save(profile);
        log.info("Suspended patient account: {}", patientId);
        return mapToDto(profile); // convert entity to DTO
    }

    public PatientProfileDTO restorePatient(Long patientId) {
        PatientProfile profile = patientProfileRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        profile.setStatus("active");
        patientProfileRepository.save(profile);
        log.info("Restored patient account: {}", patientId);
        return mapToDto(profile); // convert entity to DTO
    }

    @Override
    public void updateProfileImage(Long patientProfileId, String fileUrl) {
        PatientProfile patientProfile = patientProfileRepository
                .findByPatientProfileId(patientProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        patientProfile.setProfileImage(fileUrl);
        patientProfileRepository.save(patientProfile);
    }


    // Helper method to map entity to DTO
    private PatientProfileDTO mapToDto(PatientProfile profile) {
        return PatientProfileDTO.builder()
                .patientId(profile.getPatientProfileId())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .contactNumber(profile.getContactNumber())
                .status(profile.getStatus())
                .build();
    }



}
