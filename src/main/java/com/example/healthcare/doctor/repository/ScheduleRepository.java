package com.example.healthcare.doctor.repository;


import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.model.DoctorSchedule;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    @EntityGraph(attributePaths = {"doctorProfile"})
    List<DoctorSchedule> findByDoctorProfileId(UUID doctorProfileId);

    void deleteByDoctorProfileId(UUID doctorProfileId);

    boolean existsByDoctorProfileAndScheduleDateAndStartTime(
            DoctorProfile doctorProfile,
            LocalDate scheduleDate,
            LocalTime startTime
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            SELECT s
            FROM DoctorSchedule s
            WHERE s.doctorProfile.id = :doctorId
            AND s.scheduleDate = :date
            AND s.startTime = :startTime
            """)
    Optional<DoctorSchedule> findForUpdate(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime
    );

    @Query("""
            SELECT s
            FROM DoctorSchedule s
            WHERE s.doctorProfile.id = :doctorId
            AND s.scheduleDate = :date
            AND (
                    :startTime < s.endTime
                    AND :endTime > s.startTime
            )
            """)
    List<DoctorSchedule> findConflictingSchedules(
            @Param("doctorId") UUID doctorId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
