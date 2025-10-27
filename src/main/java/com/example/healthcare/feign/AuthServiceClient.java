package com.example.healthcare.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "auth-service")
public interface AuthServiceClient {

    @GetMapping("/admin/pending-doctors-count")
    long getPendingDoctorApprovals();
}
