package com.example.healthcare.appointment.dto.response;


import com.example.healthcare.common.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenderCountDto {
    private Gender gender;
    private long count;
}