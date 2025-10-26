package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;

    @Override
    public void createDoctorProfile(String token) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        String email = JwtUtils.extractEmailFromToken(token);

        boolean exists = doctorProfileRepository.findByUserId(userId).isPresent();
        if (!exists) {
            DoctorProfile profile = DoctorProfile.builder()
                    .userId(userId)
                    .fullName("Unknown")
                    .email(email != null ? email : "unknown@example.com")
                    .specialization(null)
                    .yearsOfExperience(0)
                    .contactNumber(null)
                    .build();

            doctorProfileRepository.save(profile);
        }
    }

    @Override
    public void updateDoctorProfile(Long userId, DoctorProfileUpdateDto dto) {
        DoctorProfile profile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        profile.setFullName(dto.getFullName());
        profile.setSpecialization(dto.getSpecialization());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setContactNumber(dto.getContactNumber());
        profile.setWorkingAT(dto.getWorkingAT());

        doctorProfileRepository.save(profile);
    }

    @Override
    public List<DoctorProfileResponseDto> getAllDoctorProfiles() {
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();

        return doctors.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<DoctorProfileResponseDto> getSpecializedDoctorProfile(String specialization) {
        List<DoctorProfile> doctors = doctorProfileRepository.findBySpecialization(specialization);

        if(doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found with specialization: " + specialization);
        }

        return doctors.stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public List<DoctorProfileResponseDto> getDoctorsByExperience(String level) {
        List<DoctorProfile> doctors = switch (level.toLowerCase()) {
            case "junior" -> doctorProfileRepository.findByYearsOfExperienceBetween(0, 3);
            case "mid", "mid-level" -> doctorProfileRepository.findByYearsOfExperienceBetween(4, 7);
            case "experienced" -> doctorProfileRepository.findByYearsOfExperienceGreaterThanEqual(8);
            default -> throw new IllegalArgumentException("Invalid experience level: " + level);
        };

        if(doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found for experience level: " + level);
        }

        return doctors.stream()
                .map(this::mapToDto)
                .toList();
    }

    // Helper method to map entity to DTO
    private DoctorProfileResponseDto mapToDto(DoctorProfile doctor) {
        return DoctorProfileResponseDto.builder()
                .doctorProfileId(doctor.getDoctorProfileId())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .workingAT(doctor.getWorkingAT())
                .contactNumber(doctor.getContactNumber())
                .build();
    }
}
