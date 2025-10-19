package com.example.healthcare.service;

import com.example.healthcare.exceptions.ResourceNotFoundException;
import com.example.healthcare.model.AppointmentRequest;
import com.example.healthcare.repository.AppointmentRequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AppointmentRequestService {

    private final AppointmentRequestRepository repository;
    private final NotificationService notificationService; // Your WebSocket + DB notification service

    public AppointmentRequestService(AppointmentRequestRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    @Transactional
    public AppointmentRequest createRequest(AppointmentRequest request) {
        AppointmentRequest savedRequest = repository.save(request);

        // Send notification to doctor
        notificationService.sendNotification(
                request.getDoctorId().toString(),
                "New Appointment Request",
                "Patient " + request.getPatientId() + " requested an appointment",
                "APPOINTMENT_REQUEST"
        );

        return savedRequest;
    }

    public List<AppointmentRequest> getRequestsForDoctor(Long doctorId) {
        return repository.findByDoctorId(doctorId);
    }

    public List<AppointmentRequest> getRequestsForPatient(Long patientId) {
        return repository.findByPatientId(patientId);
    }

    @Transactional
    public AppointmentRequest updateStatus(Long requestId, String status) {
        AppointmentRequest request = repository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        request.setStatus(status);
        repository.save(request);

        //notify patient about status change
        notificationService.sendNotification(
                request.getPatientId().toString(),
                "Appointment " + status,
                "Your appointment with doctor " + request.getDoctorId() + " is " + status,
                "APPOINTMENT_STATUS"
        );

        return request;
    }
}
