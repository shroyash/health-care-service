package com.example.healthcare.dto.response;

import com.example.healthcare.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentStatusCountDto {

    private AppointmentStatus status;
    private Long count;

}