package com.example.healthcare.appointment.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DailyAppointmentCountDto {
    private LocalDate date;
    private Long count;

    public DailyAppointmentCountDto(Date date, Long count) {
        this.date = date.toLocalDate();
        this.count = count;
    }
}