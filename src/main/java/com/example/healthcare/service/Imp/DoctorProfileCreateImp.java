package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.DoctorProfileUpdateDto;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DoctorProfileCreateImp implements DoctorProfileService {

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
                .orElseThrow(() -> new RuntimeException("Doctor profile not found"));

        profile.setSpecialization(dto.getSpecialization());
        profile.setYearsOfExperience(dto.getYearsOfExperience());
        profile.setContactNumber(dto.getContactNumber());
        profile.setEmail(dto.getEmail());

        doctorProfileRepository.save(profile);
    }


}
