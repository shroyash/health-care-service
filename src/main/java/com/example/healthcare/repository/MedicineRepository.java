package com.example.healthcare.repository;

import com.example.healthcare.model.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findAllByOrderByCreatedAtDesc();

    List<Medicine> findByAddedByDoctorId(UUID doctorId);

    List<Medicine> findByNameContainingIgnoreCase(String name);
}