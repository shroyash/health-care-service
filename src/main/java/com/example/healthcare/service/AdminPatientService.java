package com.example.healthcare.service;

import com.example.healthcare.dto.response.PatientProfileDTO;
import com.example.healthcare.mapper.PatientProfileMapper;
import com.example.healthcare.repository.PatientProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminPatientService {

    private final PatientProfileRepository patientProfileRepository;
    private final PatientProfileMapper mapper;

    @Transactional(readOnly = true)
    public List<PatientProfileDTO> getAllPatients() {
        return patientProfileRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }
}