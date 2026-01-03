package com.example.healthcare.repository;

import com.example.healthcare.model.AppointmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByDoctorId(UUID doctorId);

    List<AppointmentRequest> findByPatientId(UUID patientId);

    @Transactional
    int deleteByRequestedAtBefore(LocalDateTime cutoff);

}
