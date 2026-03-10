package com.example.healthcare.repository;

import com.example.healthcare.model.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<MedicalReport, Long> {
    List<MedicalReport> findByPatientId(UUID patientId);
    List<MedicalReport> findByDoctorId(UUID doctorId);
    Optional<MedicalReport> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);
}
