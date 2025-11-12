package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.dto.PatientProfileDTO;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PatientProfileService {
    void createPatientProfile(String token);
    void updatePatientProfile(Long doctorId, PatientProfileUpdateDto dto);
    List<PatientProfileDTO> getAllPatients();
    PatientProfileDTO suspendPatient(Long patientId);
    PatientProfileDTO restorePatient(Long patientId);
    void updateProfileImage(Long patientProfileId, String fileUrl);

}
