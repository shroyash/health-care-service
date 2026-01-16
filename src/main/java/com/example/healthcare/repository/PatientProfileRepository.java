package com.example.healthcare.repository;

import com.example.healthcare.dto.GenderCountDto;
import com.example.healthcare.enums.Status;
import com.example.healthcare.model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, UUID> {

    long countByStatus(Status status);

    @Query("""
        SELECT new com.example.healthcare.dto.GenderCountDto(
            p.gender,
            COUNT(p)
        )
        FROM PatientProfile p
        GROUP BY p.gender
    """)
    List<GenderCountDto> countByGender();

}