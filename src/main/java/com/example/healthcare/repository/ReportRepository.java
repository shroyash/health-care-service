package com.example.healthcare.repository;

import com.example.healthcare.model.MedicalReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<MedicalReport, Long> {
    List<MedicalReport> findByPatientId(Long patientId);
    List<MedicalReport> findByDoctorId(Long doctorId);
    Optional<MedicalReport> findByAppointmentId(Long appointmentId);
    boolean existsByAppointmentId(Long appointmentId);
}