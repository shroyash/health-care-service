package com.example.healthcare.kafka;

import com.example.healthcare.dto.UserRegisteredEvent;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegisteredConsumer {

    private final DoctorProfileService doctorProfileService;
    private final PatientProfileService patientProfileService;

    @KafkaListener(
            topics = "user-registered",
            groupId = "healthcare-service"
    )
    public void consume(UserRegisteredEvent event) {

        log.info("Received UserRegisteredEvent: {}", event);

        if (event.getLicenseUrl() != null) {
            // Doctor registered
            log.info("Creating doctor profile for userId={}", event.getUserId());
            doctorProfileService.createDoctorProfile(event);
            // createDoctorProfile(event);
        } else {
            // Patient registered
            log.info("Creating patient profile for userId={}", event.getUserId());
            patientProfileService.createPatientProfile(event);
            // createPatientProfile(event);
        }
    }
}
