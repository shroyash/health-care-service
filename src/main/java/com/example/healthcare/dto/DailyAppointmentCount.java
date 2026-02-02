package com.example.healthcare.dto;

import java.time.LocalDate;

public interface DailyAppointmentCount {

    LocalDate getDate();
    Long getCount();
}
