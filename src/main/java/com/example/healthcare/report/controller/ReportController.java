package com.example.healthcare.report.controller;


import com.example.healthcare.report.dto.ReportRequestDto;
import com.example.healthcare.common.dto.ApiResponse;
import com.example.healthcare.report.dto.ReportResponseDto;
import com.example.healthcare.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ApiResponse<ReportResponseDto>> createReport(
            @RequestBody ReportRequestDto request,
            @RequestHeader("X-User-Id") UUID doctorId) {

        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report created successfully")
                .data(reportService.createReport(request, doctorId))
                .build());
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ApiResponse<ReportResponseDto>> updateReport(
            @PathVariable Long reportId,
            @RequestBody ReportRequestDto request,
            @RequestHeader("X-User-Id") UUID doctorId) {

        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report updated successfully")
                .data(reportService.updateReport(reportId, request, doctorId))
                .build());
    }

    @PatchMapping("/{reportId}/finalize")
    public ResponseEntity<ApiResponse<ReportResponseDto>> finalizeReport(
            @PathVariable Long reportId,
            @RequestHeader("X-User-Id") UUID doctorId) {

        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report finalized successfully")
                .data(reportService.finalizeReport(reportId, doctorId))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getAllReports() {
        return ResponseEntity.ok(ApiResponse.<List<ReportResponseDto>>builder()
                .status(true)
                .message("Reports fetched successfully")
                .data(reportService.getAllReports())
                .build());
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ApiResponse<ReportResponseDto>> getReportById(
            @PathVariable Long reportId) {
        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report fetched successfully")
                .data(reportService.getReportById(reportId))
                .build());
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ApiResponse<ReportResponseDto>> getReportByAppointment(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report fetched successfully")
                .data(reportService.getReportByAppointmentId(appointmentId))
                .build());
    }

    @GetMapping("/patient")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByPatient(
            @RequestHeader("X-User-Id") UUID patientId) {

        return ResponseEntity.ok(ApiResponse.<List<ReportResponseDto>>builder()
                .status(true)
                .message("Reports fetched successfully")
                .data(reportService.getReportsByPatient(patientId))
                .build());
    }

    @GetMapping("/doctor")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByDoctor(
            @RequestHeader("X-User-Id") UUID doctorId) {

        return ResponseEntity.ok(ApiResponse.<List<ReportResponseDto>>builder()
                .status(true)
                .message("Reports fetched successfully")
                .data(reportService.getReportsByDoctor(doctorId))
                .build());
    }
}