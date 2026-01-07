package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.dto.DoctorRegisteredEvent;
import com.example.healthcare.dto.UserRegisteredEvent;
import com.example.healthcare.enums.Status;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;


    public void createDoctorProfile(DoctorRegisteredEvent event) {

        UUID userId = UUID.fromString(event.getUserId());

        boolean exists = doctorProfileRepository.findById(userId).isPresent();

        if (!exists) {
            DoctorProfile profile = DoctorProfile.builder()
                    .id(userId)
                    .fullName(event.getUsername())
                    .email(event.getEmail())
                    .specialization(null)
                    .yearsOfExperience(0)
                    .contactNumber(null)
                    .status(Status.ACTIVE)
                    .build();

            doctorProfileRepository.save(profile);
        }
    }


    @Override
    public DoctorProfileResponseDto getDoctorProfile(UUID userID) {
        DoctorProfile doctorProfile = doctorProfileRepository.findById(userID).
                orElseThrow(() -> new ResourceNotFoundException("doctor profile not found"));

        return mapToDto(doctorProfile);
    }

    @Override
    public void updateDoctorProfile(UUID userId, DoctorProfileUpdateDto dto) {
        DoctorProfile profile = doctorProfileRepository.findById(userId)
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
        return doctors.stream().map(this::mapToDto).toList();
    }

    @Override
    public List<DoctorProfileResponseDto> getSpecializedDoctorProfile(String specialization) {
        List<DoctorProfile> doctors = doctorProfileRepository.findBySpecialization(specialization);

        if(doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found with specialization: " + specialization);
        }

        return doctors.stream().map(this::mapToDto).toList();
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

        return doctors.stream().map(this::mapToDto).toList();
    }

    @Override
    public DoctorProfileResponseDto suspendDoctor(UUID doctorId) {
        DoctorProfile profile = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        profile.setStatus(Status.INACTIVE);
        doctorProfileRepository.save(profile);
        return mapToDto(profile);
    }

    @Override
    public DoctorProfileResponseDto restoreDoctor(UUID doctorId) {
        DoctorProfile profile = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        profile.setStatus(Status.ACTIVE);
        doctorProfileRepository.save(profile);
        return mapToDto(profile);
    }

    @Override
    public void updateProfileImage(UUID doctorId, String fileUrl) {
        DoctorProfile doctorProfile = doctorProfileRepository
                .findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found"));

        doctorProfile.setProfileImage(fileUrl);
        doctorProfileRepository.save(doctorProfile);
    }


    // ----------------- Helper -----------------
    private DoctorProfileResponseDto mapToDto(DoctorProfile doctor) {
        return DoctorProfileResponseDto.builder()
                .doctorProfileId(doctor.getId())
                .fullName(doctor.getFullName())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .workingAT(doctor.getWorkingAT())
                .contactNumber(doctor.getContactNumber())
                .profileImgUrl(doctor.getProfileImage())
                .status(doctor.getStatus().name())
                .build();
    }
}
