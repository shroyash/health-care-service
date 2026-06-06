// appointment/controller/AppointmentAccessController.java
package com.example.healthcare.appointment.controller;

import com.example.healthcare.appointment.dto.response.MeetingAccessDto;
import com.example.healthcare.appointment.service.AppointmentAccessService;
import com.example.healthcare.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentAccessController {

    private final AppointmentAccessService appointmentAccessService;

    @GetMapping("/{id}/access")
    public ResponseEntity<ApiResponse<MeetingAccessDto>> validateAccess(
            @PathVariable Long id,
            @RequestParam String token,
            @RequestHeader("X-User-Id") UUID userId) {
        return ResponseEntity.ok(new ApiResponse<>(true,
                "Meeting access validated",
                appointmentAccessService.validateAccess(id, token, userId)));
    }
}