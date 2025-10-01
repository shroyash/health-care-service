package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileCreateDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;

public interface DoctorProfileService {
    void createDoctorProfile(DoctorProfileCreateDto doctorProfileCreateDto);
    void updateDoctorProfile(Long doctorId, DoctorProfileUpdateDto dto);
}
