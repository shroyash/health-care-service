package com.example.healthcare.service;

import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.event.UserRegisteredEvent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PatientProfileService {
    void createPatientProfile(UserRegisteredEvent event);
    PatientProfileDTO getPatientProfile(UUID userId);
    void updatePatientProfile(UUID userId, PatientProfileUpdateDto dto);
    List<PatientProfileDTO> getAllPatients();
    PatientProfileDTO suspendPatient(UUID patientId);
    PatientProfileDTO restorePatient(UUID patientId);
    void updateProfileImage(UUID patientProfileId, String fileUrl);

}
