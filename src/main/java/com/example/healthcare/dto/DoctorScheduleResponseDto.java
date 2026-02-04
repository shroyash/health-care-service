package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorScheduleResponseDto {
    private String doctorName;
    private String email;
    private String specialization;
    private String contactNumber;
    private List<ScheduleInfo> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleInfo {
        private Long scheduleId;
        private LocalDate scheduleDate;
        private String dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private boolean available;
        private boolean isLocked;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
