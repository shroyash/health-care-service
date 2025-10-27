package com.example.healthcare.repository;

import com.example.healthcare.dto.DoctorAppointmentDto;
import com.example.healthcare.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment,Long> {
    // Count appointments for a doctor today
    long countByDoctorIdAndAppointmentDateBetween(Long doctorId, LocalDateTime start, LocalDateTime end);

    // Count all appointments for a doctor
    long countByDoctorId(Long doctorId);

    @Query("SELECT COUNT(DISTINCT a.patient.id) FROM Appointment a WHERE a.doctor.id = :doctorId")
    long countDistinctPatientsByDoctor(@Param("doctorId") Long doctorId);

    @Query("SELECT new com.example.healthcare.dto.DoctorAppointmentDto(" +
            "a.id, p.fullName, a.appointmentDate, a.meetingLink, s.slot) " +
            "FROM Appointment a " +
            "JOIN a.patient p " +
            "JOIN a.schedule s " +
            "WHERE a.doctor.id = :doctorId AND a.status = 'CONFIRMED' " +
            "ORDER BY a.appointmentDate ASC")
    List<DoctorAppointmentDto> findConfirmedAppointmentsWithScheduleByDoctor(@Param("doctorId") Long doctorId);

    long countByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);



}
