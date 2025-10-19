package com.example.healthcare.repository;

import com.example.healthcare.model.AppointmentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {
    List<AppointmentRequest> findByDoctorId(Long doctorId);
    List<AppointmentRequest> findByPatientId(Long patientId);
}
