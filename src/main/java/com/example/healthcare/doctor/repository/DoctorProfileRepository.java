package com.example.healthcare.doctor.repository;

import com.example.healthcare.common.enums.Status;
import com.example.healthcare.doctor.model.DoctorProfile;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
@NonNullApi
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, UUID> {

    List<DoctorProfile> findByStatus(Status status);

    List<DoctorProfile> findBySpecializationAndStatus(String specialization, Status status);

    List<DoctorProfile> findByYearsOfExperienceBetweenAndStatus(int start, int end, Status status);

    List<DoctorProfile> findByYearsOfExperienceGreaterThanEqualAndStatus(int start, Status status);

    @Query("SELECT COUNT(d) FROM DoctorProfile d WHERE d.status = :status")
    long countByStatus(@Param("status") Status status);
}
