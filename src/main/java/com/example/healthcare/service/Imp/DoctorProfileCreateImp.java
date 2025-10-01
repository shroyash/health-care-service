package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorProfileCreateDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.DoctorProfileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorProfileCreateImp implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public void createDoctorProfile(DoctorProfileCreateDto dto) {

        // Check if profile already exists
        doctorProfileRepository.findByDoctorId(dto.getDoctorId())
                .ifPresent(profile -> {
                    throw new RuntimeException("Doctor profile already exists");
                });

        // Create and save new profile
        DoctorProfile profile = DoctorProfile.builder()
                .doctorId(dto.getDoctorId())
                .fullName(dto.getUserName())
                .email(dto.getEmail())
                .specialization(null)
                .yearsOfExperience(0)
                .contactNumber(null)

                .build();

        doctorProfileRepository.save(profile);
    }

    @Override
    public void updateDoctorProfile(Long doctorId, DoctorProfileUpdateDto dto) {

        DoctorProfile profile = doctorProfileRepository.findByDoctorId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        profile.setSpecialization(dto.getSpecialization());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setContactNumber(dto.getContactNumber());
        profile.setEmail(dto.getEmail());

        doctorProfileRepository.save(profile);
    }


}
