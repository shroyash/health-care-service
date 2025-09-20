package com.example.appointment.service;

import com.example.appointment.model.DoctorRequest;
import com.example.appointment.model.DoctorRequestStatus;
import com.example.appointment.repository.DoctorRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorReqService {

    private final DoctorRequestRepository doctorRequestRepository;

    public DoctorRequest saveDoctorRequest(DoctorRequest request) {
        request.setStatus(DoctorRequestStatus.PENDING);
        request.setCreatedAt(LocalDateTime.now());
        return doctorRequestRepository.save(request);
    }


    // Fetch all pending requests for admin
    public List<DoctorRequest> getPendingRequests() {
        return doctorRequestRepository.findByStatus(DoctorRequestStatus.PENDING);
    }

    // Update request status
    public DoctorRequest updateRequestStatus(Long id, DoctorRequestStatus status) {
        Optional<DoctorRequest> optionalRequest = doctorRequestRepository.findById(id);
        if(optionalRequest.isPresent()) {
            DoctorRequest request = optionalRequest.get();
            request.setStatus(status);
            return doctorRequestRepository.save(request);
        }
        throw new RuntimeException("Doctor request not found");
    }
}
