package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.request.DoctorScheduleDto;
import com.example.healthcare.dto.response.DoctorScheduleResponseDto;
import com.example.healthcare.dto.response.DoctorScheduleUpdateDTO;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ScheduleServiceImp implements ScheduleService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final ScheduleRepository scheduleRepository;
    @Transactional
    public void saveWeeklySchedule(DoctorScheduleDto dto, UUID userId) {
        DoctorProfile doctor = doctorProfileRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + userId));

        if (dto.getSchedules() == null || dto.getSchedules().isEmpty()) {
            throw new IllegalArgumentException("No schedules provided in request");
        }

        List<DoctorSchedule> scheduleList = dto.getSchedules().stream()
                .map(s -> DoctorSchedule.builder()
                        .scheduleDate(s.getScheduleDate())
                        .startTime(s.getStartTime())
                        .endTime(s.getEndTime())
                        .available(s.isAvailable())
                        .doctorProfile(doctor)
                        .build())
                .toList();

        scheduleRepository.saveAll(scheduleList);
    }

    @Transactional(readOnly = true)
    public DoctorScheduleResponseDto getDoctorScheduleWithDetails(UUID doctorProfileId) {
        DoctorProfile doctor = doctorProfileRepository.findById(doctorProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorProfileId));

        // Only future schedules
        List<DoctorScheduleResponseDto.ScheduleInfo> scheduleInfoList = doctor.getSchedules().stream()
                .filter(s -> !s.getScheduleDate().isBefore(LocalDate.now())) // future or today
                .sorted((s1, s2) -> s1.getScheduleDate().compareTo(s2.getScheduleDate()))
                .map(schedule -> DoctorScheduleResponseDto.ScheduleInfo.builder()
                        .scheduleId(schedule.getId())
                        .scheduleDate(schedule.getScheduleDate())
                        .startTime(schedule.getStartTime())
                        .endTime(schedule.getEndTime())
                        .available(schedule.isAvailable())
                        .isLocked(schedule.isLocked())
                        .createdAt(schedule.getCreatedAt())
                        .updatedAt(schedule.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        return DoctorScheduleResponseDto.builder()
                .doctorName(doctor.getFullName())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .contactNumber(doctor.getContactNumber())
                .schedules(scheduleInfoList)
                .build();
    }

    @Override
    public void deleteSchedule(long scheduleId) {

    }

    @Override
    public DoctorSchedule updateSchedule(Long id, DoctorScheduleUpdateDTO dto) {
        return null;
    }
}