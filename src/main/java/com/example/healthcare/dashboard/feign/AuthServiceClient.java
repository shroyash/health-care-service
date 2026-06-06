package com.example.healthcare.dashboard.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/api/admin/pending-doctors-count")
    long getPendingDoctorApprovals();
}