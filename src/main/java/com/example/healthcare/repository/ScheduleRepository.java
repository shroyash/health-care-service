package com.example.healthcare.repository;

import com.example.healthcare.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Long, DoctorSchedule> {
}
