package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WeeklyAppointmentCountDto {
    private String day;
    private long count;
}
