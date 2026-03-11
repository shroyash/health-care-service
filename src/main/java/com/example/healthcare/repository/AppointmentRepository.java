package com.example.healthcare.repository;

import com.example.healthcare.dto.response.DoctorAppointmentDto;
import com.example.healthcare.dto.response.*;
import com.example.healthcare.model.Appointment;
import com.example.healthcare.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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
    SELECT new com.example.healthcare.dto.response.DoctorAppointmentDto(
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
      AND a.status = com.example.healthcare.enums.AppointmentStatus.SCHEDULED
    ORDER BY a.appointmentDate ASC
""")
    List<DoctorAppointmentDto> findUpcomingAppointmentsByDoctor(
            @Param("doctorId") UUID doctorId
    );

    @Query("""
    SELECT new com.example.healthcare.dto.response.DoctorAppointmentDto(
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
    ORDER BY a.appointmentDate DESC
""")
    List<DoctorAppointmentDto> findAllAppointmentsByDoctor(
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

    @Query("""
    SELECT new com.example.healthcare.dto.response.PatientAppointmentDto(
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
    LEFT JOIN a.schedule s
    WHERE a.patient.id = :patientId
    ORDER BY a.appointmentDate DESC
""")
    List<PatientAppointmentDto> findAllAppointmentsByPatient(
            @Param("patientId") UUID patientId
    );

    @Query("""
    SELECT new com.example.healthcare.dto.response.PatientAppointmentDto(
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
    LEFT JOIN a.schedule s
    WHERE a.patient.id = :patientId
      AND a.status = com.example.healthcare.enums.AppointmentStatus.SCHEDULED
      AND a.appointmentDate > :now
    ORDER BY a.appointmentDate ASC
""")
    List<PatientAppointmentDto> findUpcomingAppointmentsByPatient(
            @Param("patientId") UUID patientId,
            @Param("now") LocalDateTime now
    );


    // Completed or cancelled appointments for patient
    @Query("""
        SELECT new com.example.healthcare.dto.response.PatientAppointmentDto(
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
        SELECT new com.example.healthcare.dto.response.AppointmentFullDto(
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

    @Query("""
SELECT new com.example.healthcare.dto.response.WeeklyAppointmentCountDto(
    TRIM(TO_CHAR(a.appointmentDate, 'Day')),
    COUNT(a.id)
)
FROM Appointment a
WHERE a.appointmentDate BETWEEN :start AND :end
GROUP BY TRIM(TO_CHAR(a.appointmentDate, 'Day'))
ORDER BY MIN(a.appointmentDate)
""")
    List<WeeklyAppointmentCountDto> countAppointmentsByDayOfWeek(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    @Query("""
    SELECT 
        DATE(a.appointmentDate) AS date,
        COUNT(a.id) AS count
    FROM Appointment a
    WHERE a.doctor.id = :doctorId
    AND a.appointmentDate BETWEEN :startDate AND :endDate
    GROUP BY DATE(a.appointmentDate)
    ORDER BY DATE(a.appointmentDate)
""")
    List<DailyAppointmentCount> getDoctorWeeklyAppointmentCount(
            UUID doctorId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    @Query("""
    SELECT 
        DATE(a.appointmentDate) AS date,
        COUNT(a.id) AS count
    FROM Appointment a
    WHERE a.patient.id = :patientId
    AND a.appointmentDate BETWEEN :startDate AND :endDate
    GROUP BY DATE(a.appointmentDate)
    ORDER BY DATE(a.appointmentDate)
""")
    List<DailyAppointmentCount> getPatientWeeklyAppointmentCount(
            UUID patientId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    @Query("""
        SELECT new com.example.healthcare.dto.response.CheckupTypeCountDto(
            a.checkupType,
            COUNT(a.id)
        )
        FROM Appointment a
        WHERE a.doctor.id = :doctorId
        GROUP BY a.checkupType
        ORDER BY a.checkupType
    """)
    List<CheckupTypeCountDto> countAppointmentsByCheckupType(
            @Param("doctorId") UUID doctorId
    );

    @Query("""
            SELECT new com.example.healthcare.dto.response.DoctorAppointmentDto(
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
              AND a.status IN (
                com.example.healthcare.enums.AppointmentStatus.CANCELLED,
                com.example.healthcare.enums.AppointmentStatus.COMPLETED
              )
            ORDER BY a.appointmentDate DESC
            """)
    List<DoctorAppointmentDto> findDoctorHistory(@Param("doctorId") UUID doctorId);

    @Query("""
            SELECT new com.example.healthcare.dto.response.PatientAppointmentDto(
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
            LEFT JOIN a.schedule s
            WHERE a.patient.id = :patientId
              AND a.status IN (
                com.example.healthcare.enums.AppointmentStatus.CANCELLED,
                com.example.healthcare.enums.AppointmentStatus.COMPLETED
              )
            ORDER BY a.appointmentDate DESC
            """)
    List<PatientAppointmentDto> findPatientHistory(@Param("patientId") UUID patientId);

    @Modifying
    @Query(value = """
        UPDATE appointment a
        SET status = 'CANCELLED'
        FROM doctor_schedule s
        WHERE a.schedule_id = s.id
          AND a.doctor_id = :doctorId
          AND a.status = 'SCHEDULED'
          AND (a.appointment_date + s.end_time) < :now
        """, nativeQuery = true)
    int cancelExpiredForDoctor(@Param("doctorId") UUID doctorId,
                               @Param("now") LocalDateTime now);

    @Modifying
    @Query(value = """
        UPDATE appointment a
        SET status = 'CANCELLED'
        FROM doctor_schedule s
        WHERE a.schedule_id = s.id
          AND a.patient_id = :patientId
          AND a.status = 'SCHEDULED'
          AND (a.appointment_date + s.end_time) < :now
        """, nativeQuery = true)
    int cancelExpiredForPatient(@Param("patientId") UUID patientId,
                                @Param("now") LocalDateTime now);

    @Query("""
    SELECT new com.example.healthcare.dto.response.AppointmentStatusCountDto(
        a.status, COUNT(a)
    )
    FROM Appointment a
    WHERE a.patient.id = :patientId
    GROUP BY a.status
""")
    List<AppointmentStatusCountDto> getStatusCountByPatient(UUID patientId);
}
