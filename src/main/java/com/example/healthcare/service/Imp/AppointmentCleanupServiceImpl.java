package com.example.healthcare.service.Imp;

import com.example.healthcare.repository.AppointmentRequestRepository;
import com.example.healthcare.service.AppointmentCleanupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentCleanupServiceImpl implements AppointmentCleanupService {

    private final AppointmentRequestRepository repository;

    @Override
    @Scheduled(fixedRate = 60 * 60 * 1000) // every hour
    public void deleteOldRequests() {
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        int deletedCount = repository.deleteByRequestedAtBefore(cutoff);

        if (deletedCount > 0) {
            log.info("Deleted {} appointment requests older than 24 hours.", deletedCount);
        } else {
            log.debug("No expired appointment requests found for deletion.");
        }
    }
}
