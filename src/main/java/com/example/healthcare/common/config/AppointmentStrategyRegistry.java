package com.example.healthcare.common.config;

import com.example.healthcare.common.service.strategy.AppointmentRangeStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppointmentStrategyRegistry {

    private final Map<String, AppointmentRangeStrategy> strategies;

    public AppointmentRangeStrategy get(String range) {
        AppointmentRangeStrategy strategy = strategies.get(range);
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unknown range: " + range + ". Supported: weekly, monthly, yearly"
            );
        }
        return strategy;
    }
}