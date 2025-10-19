package com.example.healthcare.controller;

import com.example.healthcare.model.AppointmentRequest;
import com.example.healthcare.service.AppointmentRequestService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentRequestController {

    private final AppointmentRequestService service;

    public AppointmentRequestController(AppointmentRequestService service) {
        this.service = service;
    }

    // Patient creates appointment request
    @PostMapping("/request")
    public AppointmentRequest createRequest(@RequestBody AppointmentRequest request) {
        return service.createRequest(request);
    }

    // Get all requests for a doctor
    @GetMapping("/doctor/{doctorId}")
    public List<AppointmentRequest> getRequestsForDoctor(@PathVariable Long doctorId) {
        return service.getRequestsForDoctor(doctorId);
    }

    // Get all requests for a patient
    @GetMapping("/patient/{patientId}")
    public List<AppointmentRequest> getRequestsForPatient(@PathVariable Long patientId) {
        return service.getRequestsForPatient(patientId);
    }

    // Doctor confirms/rejects request
    @PatchMapping("/update-status/{requestId}")
    public AppointmentRequest updateStatus(@PathVariable Long requestId, @RequestParam String status) {
        return service.updateStatus(requestId, status);
    }
}
