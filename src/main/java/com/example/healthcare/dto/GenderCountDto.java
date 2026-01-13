package com.example.healthcare.dto;

import com.example.healthcare.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenderCountDto {
    private Gender gender;
    private long count;
}
