package com.noom.interview.fullstack.sleep.controller;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponse;
import com.noom.interview.fullstack.sleep.dto.SleepSummaryResponse;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST endpoints for managing user sleep logs.
 */
@RestController
@RequestMapping("/users/{userId}/sleep-logs")
public class SleepLogController {

    private final SleepLogService sleepLogService;

    public SleepLogController(SleepLogService sleepLogService) {
        this.sleepLogService = sleepLogService;
    }

    /**
     * Creates a sleep log for the previous night.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SleepLogResponse createSleepLog(
            @PathVariable UUID userId,
            @RequestBody CreateSleepLogRequest request) {

        return sleepLogService.createSleepLog(
                userId,
                request
        );
    }

    /**
     * Retrieves the latest sleep log for the user.
     */
    @GetMapping("/latest")
    public SleepLogResponse getLastNightSleep(
            @PathVariable UUID userId) {

        return sleepLogService.getLastNightSleep(userId);
    }

    /**
     * Retrieves aggregate sleep statistics for the last 30 days.
     */
    @GetMapping("/summary")
    public SleepSummaryResponse getLast30DaySummary(
            @PathVariable UUID userId) {

        return sleepLogService.getLast30DaySummary(userId);
    }
}
