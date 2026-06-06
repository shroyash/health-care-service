
package com.example.healthcare.dashboard.controller;

import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.dashboard.dto.AdminDashboardStatsDto;
import com.example.healthcare.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard/admin")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<AdminDashboardStatsDto>> getDashboardStats() {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Admin dashboard stats fetched successfully",
                adminDashboardService.getDashboardStats()));
    }
}