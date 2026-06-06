package com.example.healthcare.patient.kafka;

import com.example.healthcare.patient.event.UserRegisteredEvent;
import com.example.healthcare.patient.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientRegisteredConsumer {

    private final PatientProfileService patientProfileService;

    @KafkaListener(
            topics = "user-registered",
            groupId = "healthcare-user-group",
            containerFactory = "userKafkaListenerFactory"
    )
    public void consumePatientEvent(UserRegisteredEvent event) {
        log.info("Creating patient profile for userId={}", event.getUserId());
        patientProfileService.createPatientProfile(event);
    }
}
