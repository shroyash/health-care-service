package com.example.healthcare.repository;

import com.example.healthcare.dto.DoctorProfileResponseDto;
import com.example.healthcare.model.DoctorProfile;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@NonNullApi
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile,Long > {
    Optional<DoctorProfile> findByUserId(Long doctorId);
    Optional<DoctorProfile> findByDoctorProfileId(Long docterProfileId);
    List<DoctorProfile> findBySpecialization(String specialization);
    List<DoctorProfile> findByYearsOfExperienceBetween(int start, int end);
    List<DoctorProfile> findByYearsOfExperienceGreaterThanEqual(int years);
    long count();
    @EntityGraph(value = "DoctorProfile.withSchedules", type = EntityGraph.EntityGraphType.LOAD)
    List<DoctorProfile> findAll(); //
}

