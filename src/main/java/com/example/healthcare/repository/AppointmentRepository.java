package com.example.healthcare.repository;

import com.example.healthcare.dto.AppointmentFullDto;
import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.dto.PatientAppointmentDto;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.model.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Count appointments for a doctor today
    long countByDoctorDoctorProfileIdAndAppointmentDateBetween(Long doctorProfileId, LocalDateTime start, LocalDateTime end);

    // Count all appointments for a doctor
    long countByDoctorDoctorProfileId(Long doctorProfileId);

    // Count distinct patients for a doctor
    @Query("SELECT COUNT(DISTINCT a.patient.patientProfileId) FROM Appointment a WHERE a.doctor.doctorProfileId = :doctorProfileId")
    long countDistinctPatientsByDoctor(@Param("doctorProfileId") Long doctorProfileId);

    @Query("""
SELECT new com.example.healthcare.dto.DoctorAppointmentDto(
    a.id, p.id, p.fullName, a.appointmentDate, s.startTime, s.endTime,
    a.checkupType, a.meetingLink)
FROM Appointment a
JOIN a.patient p
JOIN a.schedule s
WHERE a.doctor.doctorProfileId = :doctorProfileId
AND a.status = 'CONFIRMED'
AND FUNCTION('DATE', a.appointmentDate) = CURRENT_DATE
ORDER BY a.appointmentDate ASC
""")
    List<DoctorAppointmentDto> findTodaysConfirmedAppointmentsByDoctor(@Param("doctorProfileId") Long doctorProfileId);


    // Count appointments between time range
    long countByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    long countByStatus(AppointmentStatus status);

    // Count upcoming confirmed appointments for a patient
    @Query("SELECT COUNT(a) FROM Appointment a " +
            "WHERE a.patient.patientProfileId = :patientProfileId AND a.status = 'CONFIRMED' " +
            "AND a.appointmentDate > CURRENT_TIMESTAMP")
    long countUpcomingAppointmentsByPatient(@Param("patientProfileId") Long patientProfileId);

    @Query("SELECT new com.example.healthcare.dto.PatientAppointmentDto(" +
            "a.id, d.doctorProfileId, d.fullName, a.appointmentDate, s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status) " +
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "JOIN a.schedule s " +
            "WHERE a.patient.patientProfileId = :patientProfileId " +
            "AND a.status = 'CONFIRMED' " +
            "AND a.appointmentDate > CURRENT_TIMESTAMP " +
            "ORDER BY a.appointmentDate ASC")
    List<PatientAppointmentDto> findUpcomingAppointmentsByPatient(@Param("patientProfileId") Long patientProfileId);

    // Fetch completed or cancelled appointments for a patient
    @Query("SELECT new com.example.healthcare.dto.PatientAppointmentDto(" +
            "a.id, d.fullName, a.appointmentDate, a.status, a.meetingLink) " +
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "WHERE a.patient.patientProfileId = :patientProfileId " +
            "AND (a.status = 'COMPLETED' OR a.status = 'CANCELLED') " +
            "ORDER BY a.appointmentDate DESC")
    List<PatientAppointmentDto> findCompletedOrCancelledAppointmentsByPatient(@Param("patientProfileId") Long patientProfileId);

    // Fetch all appointments with doctor and patient
    @Query("SELECT new com.example.healthcare.dto.AppointmentFullDto(" +
            "a.id, d.fullName, p.fullName, a.appointmentDate, a.status, a.meetingLink) " +
            "FROM Appointment a " +
            "JOIN a.doctor d " +
            "JOIN a.patient p " +
            "ORDER BY a.appointmentDate DESC")
    List<AppointmentFullDto> findAllAppointmentsWithDoctorAndPatient();
}

