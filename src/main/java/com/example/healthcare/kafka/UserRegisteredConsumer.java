package com.example.healthcare.kafka;

import com.example.healthcare.dto.DoctorRegisteredEvent;
import com.example.healthcare.dto.UserRegisteredEvent;
import com.example.healthcare.service.DoctorProfileService;
import com.example.healthcare.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredConsumer {

    private final PatientProfileService patientProfileService;
    private final DoctorProfileService doctorProfileService;

    @KafkaListener(topics = "patient-registered", groupId = "healthcare-service")
    public void consumePatientEvent(UserRegisteredEvent event) {
        log.info("Creating patient profile for userId={}", event.getUserId());
        patientProfileService.createPatientProfile(event);
    }

    @KafkaListener(topics = "doctor-registered", groupId = "healthcare-service")
    public void consumeDoctorEvent(DoctorRegisteredEvent event) {
        log.info("Creating doctor profile for userId={}", event.getUserId());
        doctorProfileService.createDoctorProfile(event);
    }
}

