package com.example.healthcare.repository;

import com.example.healthcare.model.DoctorSchedule;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<DoctorSchedule,Long> {

    @EntityGraph("DoctorProfile.withSchedules")
    List<DoctorSchedule> findByDoctorProfileDoctorProfileId(Long doctorProfileId);

    void deleteByDoctorProfileDoctorProfileId(Long doctorProfileId);
}
