package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.DoctorScheduleDto;
import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.DoctorProfile;
import com.example.healthcare.model.DoctorSchedule;
import com.example.healthcare.repository.DoctorProfileRepository;
import com.example.healthcare.repository.ScheduleRepository;
import com.example.healthcare.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleServiceImp implements ScheduleService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public void saveWeeklySchedule(DoctorScheduleDto dto) {
        try {
            log.info("Starting to save weekly schedule for doctor: {}", dto.getDoctorProfileId());

            // Step 1: Find doctor
            DoctorProfile doctor = doctorProfileRepository.findByDoctorProfileId(dto.getDoctorProfileId())
                    .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + dto.getDoctorProfileId()));

            log.info("Found doctor: {}", doctor.getFullName());

            // Step 2: Validate DTO
            if (dto.getSchedules() == null || dto.getSchedules().isEmpty()) {
                throw new IllegalArgumentException("No schedules provided in request");
            }

            log.info("Creating {} new schedules", dto.getSchedules().size());

            // Step 3: Delete old schedules for this doctor
            scheduleRepository.deleteByDoctorProfileDoctorProfileId(dto.getDoctorProfileId());
            log.info("Deleted old schedules for doctor");

            // Step 4: Convert DTO to entities and save directly
            List<DoctorSchedule> scheduleList = dto.getSchedules().stream().map(s -> {
                try {
                    log.debug("Creating schedule for day: {}", s.getDayOfWeek());

                    DoctorSchedule schedule = DoctorSchedule.builder()
                            .dayOfWeek(s.getDayOfWeek())
                            .startTime(s.getStartTime())
                            .endTime(s.getEndTime())
                            .available(s.isAvailable())
                            .doctorProfile(doctor)
                            .build();

                    log.debug("Schedule created successfully: {}", s.getDayOfWeek());
                    return schedule;
                } catch (Exception e) {
                    log.error("Error creating schedule for day {}: {}", s.getDayOfWeek(), e.getMessage(), e);
                    throw new RuntimeException("Error creating schedule for " + s.getDayOfWeek() + ": " + e.getMessage(), e);
                }
            }).toList();

            // Step 5: Save schedules directly (no doctor merge)
            log.info("Saving {} schedules", scheduleList.size());
            scheduleRepository.saveAll(scheduleList);

            log.info("Successfully saved weekly schedule for doctor: {}", dto.getDoctorProfileId());

        } catch (ResourceNotFoundException e) {
            log.error("Doctor not found: {}", e.getMessage());
            throw e;
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while saving weekly schedule: {}", e.getMessage(), e);
            if (e.getCause() != null) {
                log.error("Caused by: {}", e.getCause().getMessage(), e.getCause());
            }
            throw new RuntimeException("Error saving schedule: " + e.getMessage(), e);
        }
    }

    @Override
    public List<DoctorSchedule> getDoctorSchedule(Long doctorProfileId) {
        try {
            log.info("Fetching schedules for doctor: {}", doctorProfileId);

            List<DoctorSchedule> doctorSchedules = scheduleRepository.findByDoctorProfileDoctorProfileId(doctorProfileId);

            if (doctorSchedules.isEmpty()) {
                log.warn("No schedules found for doctor: {}", doctorProfileId);
                throw new ResourceNotFoundException("No schedules found for doctor profile id: " + doctorProfileId);
            }

            log.info("Found {} schedules for doctor: {}", doctorSchedules.size(), doctorProfileId);
            return doctorSchedules;

        } catch (Exception e) {
            log.error("Error fetching schedules for doctor {}: {}", doctorProfileId, e.getMessage(), e);
            throw e;
        }
    }
}