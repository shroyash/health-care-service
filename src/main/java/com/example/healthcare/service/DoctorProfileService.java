package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileUpdateDto;

public interface DoctorProfileService {
    void createDoctorProfile(String token);
    void updateDoctorProfile(Long doctorId, DoctorProfileUpdateDto dto);
}
