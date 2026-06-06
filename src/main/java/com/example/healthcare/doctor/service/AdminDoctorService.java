package com.example.healthcare.doctor.service;

import com.example.healthcare.common.exceptions.ResourceNotFoundException;
import com.example.healthcare.doctor.mapper.DoctorProfileMapper;
import com.example.healthcare.doctor.model.DoctorProfile;
import com.example.healthcare.doctor.repository.DoctorProfileRepository;
import com.example.healthcare.doctor.dto.DoctorProfileResponseDto;
import com.example.healthcare.common.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminDoctorService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final DoctorProfileMapper mapper;

    @Transactional(readOnly = true)
    public List<DoctorProfileResponseDto> getAllDoctors() {
        return doctorProfileRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public DoctorProfileResponseDto suspendDoctor(UUID doctorId) {
        DoctorProfile profile = findDoctorOrThrow(doctorId);
        profile.setStatus(Status.INACTIVE);
        doctorProfileRepository.save(profile);
        log.info("Suspended doctor: {}", doctorId);
        return mapper.toDto(profile);
    }

    @Transactional
    public DoctorProfileResponseDto restoreDoctor(UUID doctorId) {
        DoctorProfile profile = findDoctorOrThrow(doctorId);
        profile.setStatus(Status.ACTIVE);
        doctorProfileRepository.save(profile);
        log.info("Restored doctor: {}", doctorId);
        return mapper.toDto(profile);
    }

    // ── private helpers ──────────────────────────────────────────

    private DoctorProfile findDoctorOrThrow(UUID id) {
        return doctorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found"
                ));
    }
}