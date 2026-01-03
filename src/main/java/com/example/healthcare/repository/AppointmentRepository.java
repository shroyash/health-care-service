package com.example.healthcare.repository;

import com.example.healthcare.dto.AppointmentFullDto;
import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Count appointments for a doctor today
    long countByDoctorIdAndAppointmentDateBetween(
            UUID doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    // Count all appointments for a doctor
    long countByDoctorId(UUID doctorId);

    // Count distinct patients for a doctor
    @Query("""
        SELECT COUNT(DISTINCT a.patient.id)
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
    """)
    long countDistinctPatientsByDoctor(@Param("doctorId") UUID doctorId);

    // Upcoming appointments for doctor
    @Query("""
        SELECT new com.example.healthcare.dto.DoctorAppointmentDto(
            a.id,
            p.id,
            p.fullName,
            a.appointmentDate,
            s.startTime,
            s.endTime,
            a.checkupType,
            a.meetingLink,
            a.status
        )
        FROM Appointment a
        JOIN a.patient p
        LEFT JOIN a.schedule s
        WHERE a.doctor.id = :doctorId
        AND a.status = 'SCHEDULED'
        ORDER BY a.appointmentDate ASC
    """)
    List<DoctorAppointmentDto> findUpcomingAppointmentsByDoctor(
            @Param("doctorId") UUID doctorId
    );

    // Count appointments in time range
    long countByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(AppointmentStatus status);

    // Count upcoming confirmed appointments for a patient
    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.patient.id = :patientId
        AND a.status = 'CONFIRMED'
        AND a.appointmentDate > CURRENT_TIMESTAMP
    """)
    long countUpcomingAppointmentsByPatient(@Param("patientId") UUID patientId);

    // Upcoming appointments for patient
    @Query("""
        SELECT new com.example.healthcare.dto.PatientAppointmentDto(
            a.id,
            d.id,
            d.fullName,
            a.appointmentDate,
            s.startTime,
            s.endTime,
            a.checkupType,
            a.meetingLink,
            a.status
        )
        FROM Appointment a
        JOIN a.doctor d
        JOIN a.schedule s
        WHERE a.patient.id = :patientId
        AND a.status = 'SCHEDULED'
        AND a.appointmentDate > CURRENT_TIMESTAMP
        ORDER BY a.appointmentDate ASC
    """)
    List<PatientAppointmentDto> findUpcomingAppointmentsByPatient(
            @Param("patientId") UUID patientId
    );

    // Completed or cancelled appointments for patient
    @Query("""
        SELECT new com.example.healthcare.dto.PatientAppointmentDto(
            a.id,
            d.id,
            d.fullName,
            a.appointmentDate,
            null,
            null,
            a.checkupType,
            a.meetingLink,
            a.status
        )
        FROM Appointment a
        JOIN a.doctor d
        WHERE a.patient.id = :patientId
        AND a.status IN ('COMPLETED', 'CANCELLED')
        ORDER BY a.appointmentDate DESC
    """)
    List<PatientAppointmentDto> findCompletedOrCancelledAppointmentsByPatient(
            @Param("patientId") UUID patientId
    );

    // Admin / full view
    @Query("""
        SELECT new com.example.healthcare.dto.AppointmentFullDto(
            a.id,
            d.fullName,
            p.fullName,
            a.appointmentDate,
            a.status,
            a.checkupType,
            a.meetingLink
        )
        FROM Appointment a
        LEFT JOIN a.doctor d
        LEFT JOIN a.patient p
        ORDER BY a.appointmentDate DESC
    """)
    List<AppointmentFullDto> findAllAppointmentsWithDoctorAndPatient();
}
