package com.example.healthcare.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {
    // Get pending doctor requests count
    @GetMapping("/api/admin/pending-doctors-count")
    long getPendingDoctorApprovals();
}
