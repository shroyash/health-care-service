package com.example.healthcare.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorWithScheduleDto {

    private UUID doctorProfileId;
    private String name;
    private String specialty;
    private String email;
    private String phone;

    private List<ScheduleDto> schedules;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleDto {
        private String dayOfWeek;   // e.g., "Monday"
        private String startTime;   // e.g., "10:00"
        private String endTime;     // e.g., "16:00"
        private boolean available;  // true if the doctor is available in this slot
    }
}
