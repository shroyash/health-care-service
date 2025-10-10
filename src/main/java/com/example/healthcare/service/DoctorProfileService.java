package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;

import java.util.List;

public interface DoctorProfileService {
    void createDoctorProfile(String token);
    void updateDoctorProfile(Long doctorId, DoctorProfileUpdateDto dto);
    List<DoctorProfileResponseDto> getAllDoctorProfiles();
    List<DoctorProfileResponseDto>  getSpecializedDoctorProfile(String specialization);
    List<DoctorProfileResponseDto> getDoctorsByExperience(String level);
}
