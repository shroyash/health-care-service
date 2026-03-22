package com.example.healthcare.repository;

import com.example.healthcare.model.Appointment;
import com.example.healthcare.model.AppointmentRequest;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRequestRepository extends JpaRepository<AppointmentRequest, Long> {

    List<AppointmentRequest> findByDoctorId(UUID doctorId);

    List<AppointmentRequest> findByPatientId(UUID patientId);

    @Transactional
    int deleteByRequestedAtBefore(LocalDateTime cutoff);

    @Query("""
    SELECT COUNT(ar)
    FROM AppointmentRequest ar
    WHERE ar.doctorId = :doctorId
      AND ar.status = com.example.healthcare.enums.AppointmentRequestStatus.PENDING
""")
    long countPendingAppointmentRequestsByDoctor(
            @Param("doctorId") UUID doctorId
    );

    boolean existsByPatientIdAndDoctorIdAndDateAndStartTime(
            UUID patientId,
            UUID doctorId,
            LocalDate date,
            LocalTime startTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT r FROM AppointmentRequest r WHERE r.id = :id")
    Optional<AppointmentRequest> findByIdForUpdate(@Param("id") Long id);



}
