package com.example.healthcare.service.Imp;


import com.example.healthcare.dto.PatientProfileUpdateDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.PatientProfile;
import com.example.healthcare.repository.PatientProfileRepository;
import com.example.healthcare.service.PatientProfileService;
import com.example.healthcare.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class PatientProfileCreateImp implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;

    @Override
    public void createPatientProfile(String token) {
        Long userId = JwtUtils.extractUserIdFromToken(token);
        String userName = JwtUtils.extractUserNameFromToken(token);
        String email = JwtUtils.extractEmailFromToken(token);

        boolean exists = patientProfileRepository.findByUserId(userId).isPresent();
        if (!exists) {
            PatientProfile profile = PatientProfile.builder()
                    .userId(userId)
                    .fullName(userName)
                    .email(email)
                    .contactNumber(null)
                    .build();
            log.info("patient profile:{}", profile);

            patientProfileRepository.save(profile);
        }
    }

    @Override
    public void updatePatientProfile(Long userId, PatientProfileUpdateDto dto) {
        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));

        profile.setFullName(dto.getFullname());
        profile.setContactNumber(dto.getContactNumber());

        patientProfileRepository.save(profile);
    }
}
