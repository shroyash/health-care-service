package com.example.healthcare.appointment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailyAppointmentCountDto {
    private String date;  // formatted as "YYYY-MM-DD"
    private Long count;
}