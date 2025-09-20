package com.example.appointment.controller;

import com.example.appointment.model.DoctorRequest;
import com.example.appointment.model.DoctorRequestStatus;
import com.example.appointment.service.DoctorReqService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private final DoctorReqService doctorReqService;

    // ✅ 1. Fetch all pending doctor requests
    @GetMapping("/doctor-requests")
    public ResponseEntity<List<DoctorRequest>> getPendingDoctorRequests() {
        List<DoctorRequest> requests = doctorReqService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }

    // ✅ 2. Approve or reject a doctor request
    @PutMapping("/doctor-requests/{id}")
    public ResponseEntity<DoctorRequest> updateDoctorRequestStatus(
            @PathVariable Long id,
            @RequestParam DoctorRequestStatus status
    ) {
        DoctorRequest updatedRequest = doctorReqService.updateRequestStatus(id, status);
        return ResponseEntity.ok(updatedRequest);
    }
}
