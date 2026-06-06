package com.example.healthcare.appointment.dto.response;

import com.example.healthcare.common.enums.DoctorCheckType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckupTypeCountDto {
    private DoctorCheckType checkupType;
    private Long count;
}