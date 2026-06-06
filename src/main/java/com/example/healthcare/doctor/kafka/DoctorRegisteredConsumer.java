package com.example.healthcare.doctor.kafka;

import com.example.healthcare.doctor.event.DoctorRegisteredEvent;
import com.example.healthcare.doctor.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorRegisteredConsumer {

    private final DoctorProfileService doctorProfileService;

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
