
package com.example.healthcare.appointment.repository;

import com.example.healthcare.appointment.dto.response.*;
import com.example.healthcare.appointment.enums.AppointmentStatus;
import com.example.healthcare.appointment.model.Appointment;
import com.example.healthcare.appointment.dto.response.WeeklyAppointmentCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // ── Doctor ────────────────────────────────────────────────────

    long countByDoctorIdAndAppointmentDateBetween(
            UUID doctorId,
            LocalDateTime start,
            LocalDateTime end);

    @Query("""
        SELECT COUNT(DISTINCT a.patient.id)
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
    """)
    long countDistinctPatientsByDoctor(
            @Param("doctorId") UUID doctorId);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.DoctorAppointmentDto(
            a.id, p.id, p.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.patient p
        LEFT JOIN a.schedule s
        WHERE a.doctor.id = :doctorId
          AND a.status = com.example.healthcare.appointment.enums.AppointmentStatus.SCHEDULED
          AND a.appointmentDate > :now
        ORDER BY a.appointmentDate ASC
    """)
    List<DoctorAppointmentDto> findUpcomingByDoctor(
            @Param("doctorId") UUID doctorId,
            @Param("now") LocalDateTime now);


    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.DoctorAppointmentDto(
            a.id, p.id, p.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.patient p
        LEFT JOIN a.schedule s
        WHERE a.doctor.id = :doctorId
        ORDER BY a.appointmentDate DESC
    """)
    Page<DoctorAppointmentDto> findAllByDoctor(
            @Param("doctorId") UUID doctorId,
            Pageable pageable);


    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.DoctorAppointmentDto(
            a.id, p.id, p.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.patient p
        LEFT JOIN a.schedule s
        WHERE a.doctor.id = :doctorId
          AND a.status IN (
            com.example.healthcare.appointment.enums.AppointmentStatus.CANCELLED,
            com.example.healthcare.appointment.enums.AppointmentStatus.COMPLETED
          )
        ORDER BY a.appointmentDate DESC
    """)
    Page<DoctorAppointmentDto> findHistoryByDoctor(
            @Param("doctorId") UUID doctorId,
            Pageable pageable);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.CheckupTypeCountDto(
            a.checkupType, COUNT(a.id)
        )
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
        GROUP BY a.checkupType
        ORDER BY a.checkupType
    """)
    List<CheckupTypeCountDto> countByCheckupType(
            @Param("doctorId") UUID doctorId);

    // ── Patient ───────────────────────────────────────────────────

    @Query("""
        SELECT COUNT(a)
        FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.status = com.example.healthcare.appointment.enums.AppointmentStatus.SCHEDULED
          AND a.appointmentDate > CURRENT_TIMESTAMP
    """)
    long countUpcomingByPatient(
            @Param("patientId") UUID patientId);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.PatientAppointmentDto(
            a.id, d.id, d.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.doctor d
        LEFT JOIN a.schedule s
        WHERE a.patient.id = :patientId
          AND a.status = com.example.healthcare.appointment.enums.AppointmentStatus.SCHEDULED
          AND a.appointmentDate > :now
        ORDER BY a.appointmentDate ASC
    """)
    List<PatientAppointmentDto> findUpcomingByPatient(
            @Param("patientId") UUID patientId,
            @Param("now") LocalDateTime now);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.PatientAppointmentDto(
            a.id, d.id, d.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.doctor d
        LEFT JOIN a.schedule s
        WHERE a.patient.id = :patientId
        ORDER BY a.appointmentDate DESC
    """)
    Page<PatientAppointmentDto> findAllByPatient(
            @Param("patientId") UUID patientId,
            Pageable pageable);


    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.PatientAppointmentDto(
            a.id, d.id, d.fullName, a.appointmentDate,
            s.startTime, s.endTime, a.checkupType, a.meetingLink, a.status
        )
        FROM Appointment a
        JOIN a.doctor d
        LEFT JOIN a.schedule s
        WHERE a.patient.id = :patientId
          AND a.status IN (
            com.example.healthcare.appointment.enums.AppointmentStatus.CANCELLED,
            com.example.healthcare.appointment.enums.AppointmentStatus.COMPLETED
          )
        ORDER BY a.appointmentDate DESC
    """)
    Page<PatientAppointmentDto> findHistoryByPatient(
            @Param("patientId") UUID patientId,
            Pageable pageable);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.AppointmentStatusCountDto(
            a.status, COUNT(a)
        )
        FROM Appointment a
        WHERE a.patient.id = :patientId
        GROUP BY a.status
    """)
    List<AppointmentStatusCountDto> countStatusByPatient(
            @Param("patientId") UUID patientId);

    // ── Analytics ─────────────────────────────────────────────────

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.DailyAppointmentCountDto(
            CAST(a.appointmentDate AS date), COUNT(a.id)
        )
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
          AND a.appointmentDate BETWEEN :start AND :end
        GROUP BY CAST(a.appointmentDate AS date)
        ORDER BY CAST(a.appointmentDate AS date)
    """)
    List<DailyAppointmentCountDto> countByDoctorAndRange(
            @Param("doctorId") UUID doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.DailyAppointmentCountDto(
            CAST(a.appointmentDate AS date), COUNT(a.id)
        )
        FROM Appointment a
        WHERE a.patient.id = :patientId
          AND a.appointmentDate BETWEEN :start AND :end
        GROUP BY CAST(a.appointmentDate AS date)
        ORDER BY CAST(a.appointmentDate AS date)
    """)
    List<DailyAppointmentCountDto> countByPatientAndRange(
            @Param("patientId") UUID patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // ── Admin ─────────────────────────────────────────────────────

    long countByAppointmentDateBetween(
            LocalDateTime start,
            LocalDateTime end);

    long countByStatus(AppointmentStatus status);

    // paginated — admin views all appointments
    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.AppointmentFullDto(
            a.id, d.fullName, p.fullName,
            a.appointmentDate, a.status, a.checkupType, a.meetingLink
        )
        FROM Appointment a
        LEFT JOIN a.doctor d
        LEFT JOIN a.patient p
        ORDER BY a.appointmentDate DESC
    """)
    Page<AppointmentFullDto> findAllWithDetails(Pageable pageable);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.WeeklyAppointmentCountDto(
            TRIM(TO_CHAR(a.appointmentDate, 'Day')), COUNT(a.id)
        )
        FROM Appointment a
        WHERE a.appointmentDate BETWEEN :start AND :end
        GROUP BY TRIM(TO_CHAR(a.appointmentDate, 'Day'))
        ORDER BY MIN(a.appointmentDate)
    """)
    List<WeeklyAppointmentCountDto> countByDayOfWeek(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    // ── Expiry ────────────────────────────────────────────────────

    @Modifying
    @Query("""
        UPDATE Appointment a
        SET a.status = com.example.healthcare.appointment.enums.AppointmentStatus.CANCELLED
        WHERE a.doctor.id = :doctorId
          AND a.status = com.example.healthcare.appointment.enums.AppointmentStatus.SCHEDULED
          AND a.appointmentDate < :now
    """)
    int cancelExpiredForDoctor(
            @Param("doctorId") UUID doctorId,
            @Param("now") LocalDateTime now);

    @Modifying
    @Query("""
        UPDATE Appointment a
        SET a.status = com.example.healthcare.appointment.enums.AppointmentStatus.CANCELLED
        WHERE a.patient.id = :patientId
          AND a.status = com.example.healthcare.appointment.enums.AppointmentStatus.SCHEDULED
          AND a.appointmentDate < :now
    """)
    int cancelExpiredForPatient(
            @Param("patientId") UUID patientId,
            @Param("now") LocalDateTime now);

    @Query("""
    SELECT new com.example.healthcare.appointment.dto.response.StatusCountResponseDto(
        a.status,
        COUNT(a)
    )
    FROM Appointment a
    WHERE a.patient.id = :patientId
    GROUP BY a.status
""")
    List<StatusCountResponseDto> getAppointmentStatusCountByPatient(UUID patientId);

}