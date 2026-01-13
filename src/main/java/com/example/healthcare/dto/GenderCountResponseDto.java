package com.example.healthcare.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenderCountResponseDto {
    private long male;
    private long female;
}
