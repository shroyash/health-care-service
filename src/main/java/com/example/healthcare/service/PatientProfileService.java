package com.example.healthcare.service;

import com.example.healthcare.dto.response.PatientProfileDTO;
import com.example.healthcare.dto.request.PatientProfileUpdateDto;
import com.example.healthcare.event.UserRegisteredEvent;

import java.util.List;
import java.util.UUID;


public interface PatientProfileService {
    void createPatientProfile(UserRegisteredEvent event);
    PatientProfileDTO getPatientProfile(UUID userId);
    void updatePatientProfile(UUID userId, PatientProfileUpdateDto dto);
    List<PatientProfileDTO> getAllPatients();
    PatientProfileDTO suspendPatient(UUID patientId);
    PatientProfileDTO restorePatient(UUID patientId);
    void updateProfileImage(UUID patientProfileId, String fileUrl);

}
