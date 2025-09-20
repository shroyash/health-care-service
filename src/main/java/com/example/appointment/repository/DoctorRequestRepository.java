package com.example.appointment.repository;

import com.example.appointment.model.DoctorRequest;
import com.example.appointment.model.DoctorRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRequestRepository extends JpaRepository<DoctorRequest, Long> {
    List<DoctorRequest> findByStatus(DoctorRequestStatus status);
}
