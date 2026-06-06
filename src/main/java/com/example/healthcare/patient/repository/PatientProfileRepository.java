package com.example.healthcare.patient.repository;


import com.example.healthcare.common.enums.Status;
import com.example.healthcare.appointment.dto.response.GenderCountDto;
import com.example.healthcare.patient.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID> {

    long countByStatus(Status status);

    @Query("""
        SELECT new com.example.healthcare.appointment.dto.response.GenderCountDto(
            p.gender,
            COUNT(p)
        )
        FROM PatientProfile p
        GROUP BY p.gender
    """)
    List<GenderCountDto> countByGender();

}