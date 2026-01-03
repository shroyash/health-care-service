package com.example.healthcare.service;

import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.dto.UserRegisteredEvent;

import java.util.List;
import java.util.UUID;

public interface DoctorProfileService {
    void createDoctorProfile(UserRegisteredEvent event);

    DoctorProfileResponseDto getDoctorProfile(UUID userID);

    void updateDoctorProfile(UUID doctorId, DoctorProfileUpdateDto dto);

    List<DoctorProfileResponseDto> getAllDoctorProfiles();

    List<DoctorProfileResponseDto> getSpecializedDoctorProfile(String specialization);

    List<DoctorProfileResponseDto> getDoctorsByExperience(String level);


    DoctorProfileResponseDto suspendDoctor(UUID doctorId);

    DoctorProfileResponseDto restoreDoctor(UUID doctorId);

    void updateProfileImage(UUID doctorId, String fileUrl);
}
