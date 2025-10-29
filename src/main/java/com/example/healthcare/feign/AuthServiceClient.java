package com.example.healthcare.feign;

import com.example.healthcare.dto.DoctorRequestDto;
import com.example.healthcare.dto.DoctorRequestResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    // Get all doctor requests
    @GetMapping("/api/admin/doctor-req/all")
    List<DoctorRequestDto> getAllDoctorRequests();

    // Get pending doctor requests
    @GetMapping("/api/admin/doctor-req/pending")
    List<DoctorRequestDto> getPendingDoctorRequests();

    // Approve or reject a doctor request
    @PostMapping("/api/admin/doctor-req/approve")
    DoctorRequestResponse approveOrRejectDoctor(
            @RequestParam("doctorReqId") Long doctorReqId,
            @RequestParam("approve") boolean approve
    );

    // Get pending doctor requests count
    @GetMapping("/api/admin/pending-doctors-count")
    long getPendingDoctorApprovals();
}
