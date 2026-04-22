package com.example.healthcare.service.Imp;

import com.example.healthcare.dto.request.DoctorScheduleDto;
import com.example.healthcare.dto.request.DoctorScheduleUpdateDTO;
import com.example.healthcare.dto.response.DoctorScheduleResponseDto;
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
import java.util.Comparator;
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

    @Override
    @Transactional
    public void saveWeeklySchedule(DoctorScheduleDto dto, UUID userId) {
        DoctorProfile doctor = findDoctorOrThrow(userId);

        if (dto.getSchedules() == null || dto.getSchedules().isEmpty()) {
            throw new IllegalArgumentException("No schedules provided in request");
        }

        for (DoctorScheduleDto.ScheduleDto s : dto.getSchedules()) {
            boolean alreadyExists = scheduleRepository.existsByDoctorProfileAndScheduleDateAndStartTime(
                    doctor, s.getScheduleDate(), s.getStartTime()
            );
            if (alreadyExists) {
                throw new IllegalArgumentException(
                        "Slot already exists: " + s.getScheduleDate() + " at " + s.getStartTime()
                );
            }
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

    @Override
    @Transactional(readOnly = true)
    public DoctorScheduleResponseDto getDoctorScheduleWithDetails(UUID doctorProfileId) {
        DoctorProfile doctor = findDoctorOrThrow(doctorProfileId);

        List<DoctorScheduleResponseDto.ScheduleInfo> scheduleInfoList = doctor.getSchedules().stream()
                .filter(s -> !s.getScheduleDate().isBefore(LocalDate.now()))
                .sorted(Comparator.comparing(DoctorSchedule::getScheduleDate))
                .map(this::toScheduleInfo)
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
    @Transactional
    public void deleteSchedule(long scheduleId) {
        DoctorSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Schedule not found with id: " + scheduleId
                ));

        if (schedule.isLocked()) {
            throw new IllegalStateException(
                    "Cannot delete a locked schedule: " + scheduleId
            );
        }

        scheduleRepository.delete(schedule);
        log.info("Deleted schedule with id: {}", scheduleId);
    }

    @Override
    @Transactional
    public DoctorScheduleResponseDto updateSchedule(Long id, DoctorScheduleUpdateDTO dto) {
        DoctorSchedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Schedule not found with id: " + id
                ));

        if (schedule.isLocked()) {
            throw new IllegalStateException(
                    "Cannot update a locked schedule: " + id
            );
        }

        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setAvailable(dto.isAvailable());

        scheduleRepository.save(schedule);

        return getDoctorScheduleWithDetails(schedule.getDoctorProfile().getId());
    }


    private DoctorProfile findDoctorOrThrow(UUID id) {
        return doctorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + id
                ));
    }

    private DoctorScheduleResponseDto.ScheduleInfo toScheduleInfo(DoctorSchedule schedule) {
        return DoctorScheduleResponseDto.ScheduleInfo.builder()
                .scheduleId(schedule.getId())
                .scheduleDate(schedule.getScheduleDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .available(schedule.isAvailable())
                .isLocked(schedule.isLocked())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}