package com.example.healthcare.kafka;

import com.example.healthcare.event.DoctorRegisteredEvent;
import com.example.healthcare.event.UserRegisteredEvent;
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

    @KafkaListener(
            topics = "user-registered",
            groupId = "healthcare-user-group",
            containerFactory = "userKafkaListenerFactory"
    )
    public void consumePatientEvent(UserRegisteredEvent event) {
        log.info("Creating patient profile for userId={}", event.getUserId());
        patientProfileService.createPatientProfile(event);
    }

    @KafkaListener(
            topics = "doctor-registered",
            groupId = "healthcare-doctor-group",
            containerFactory = "doctorKafkaListenerFactory"
    )
    public void consumeDoctorEvent(DoctorRegisteredEvent event) {
        log.info("Creating doctor profile for userId={}", event.getUserId());
        doctorProfileService.createDoctorProfile(event);
    }
}