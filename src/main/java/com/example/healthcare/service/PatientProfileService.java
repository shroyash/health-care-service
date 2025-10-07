package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.dto.PatientProfileUpdateDto;
import org.springframework.stereotype.Service;

@Service
public interface PatientProfileService {
    void createPatientProfile(String token);
    void updatePatientProfile(Long doctorId, PatientProfileUpdateDto dto);
}
