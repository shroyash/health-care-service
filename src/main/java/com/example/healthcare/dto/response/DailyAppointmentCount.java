package com.example.healthcare.dto.response;

import java.time.LocalDate;

// DailyAppointmentCount.java
public interface DailyAppointmentCount {
    LocalDate getDate();
    Long getCount();
}