package com.example.healthcare.repository;

import com.example.healthcare.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile,Long > {
    Optional<DoctorProfile> findByUserId(Long doctorId);
}

