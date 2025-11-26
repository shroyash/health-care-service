package com.example.healthcare.repository;

import com.example.healthcare.model.AppointmentRequest;
import com.example.healthcare.model.AppointmentRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByDoctorId(Long doctorId);

    List<AppointmentRequest> findByPatientId(Long patientId);

    @Transactional
    int deleteByRequestedAtBefore(LocalDateTime cutoff);

//    Long countByDoctorIdAndStatus(Long doctorId, AppointmentRequestStatus status);
}
