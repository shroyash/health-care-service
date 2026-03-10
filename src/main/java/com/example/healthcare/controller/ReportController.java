package com.example.healthcare.controller;


import com.example.healthcare.dto.request.ReportRequestDto;
import com.example.healthcare.dto.response.ApiResponse;
import com.example.healthcare.dto.response.ReportResponseDto;
import com.example.healthcare.service.ReportService;
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
            @CookieValue("jwt") String token) {
        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report created successfully")
                .data(reportService.createReport(request, token))
                .build());
    }

    @PutMapping("/{reportId}")
    public ResponseEntity<ApiResponse<ReportResponseDto>> updateReport(
            @PathVariable Long reportId,
            @RequestBody ReportRequestDto request,
            @CookieValue("jwt") String token) {
        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report updated successfully")
                .data(reportService.updateReport(reportId, request, token))
                .build());
    }

    @PatchMapping("/{reportId}/finalize")
    public ResponseEntity<ApiResponse<ReportResponseDto>> finalizeReport(
            @PathVariable Long reportId,
            @CookieValue("jwt") String token) {
        return ResponseEntity.ok(ApiResponse.<ReportResponseDto>builder()
                .status(true)
                .message("Report finalized successfully")
                .data(reportService.finalizeReport(reportId, token))
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

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByPatient(
            @PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.<List<ReportResponseDto>>builder()
                .status(true)
                .message("Reports fetched successfully")
                .data(reportService.getReportsByPatient(patientId))
                .build());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<ApiResponse<List<ReportResponseDto>>> getReportsByDoctor(
            @PathVariable UUID doctorId) {
        List<ReportResponseDto> reportResponseDtos = reportService.getReportsByDoctor(doctorId);
        return ResponseEntity.ok(ApiResponse.<List<ReportResponseDto>>builder()
                .status(true)
                .message("Reports fetched successfully")
                .data(reportResponseDtos)
                .build());
    }
}
