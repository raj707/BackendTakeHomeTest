package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.*;

import java.util.UUID;

/**
 * Service interface for managing business logic around user sleep log.
 */
public interface SleepLogService {

    /**
     * Creates a sleep log for the user's last night sleep.
     */
    SleepLogResponse createSleepLog(
            UUID userId,
            CreateSleepLogRequest request
    );


    /**
     * Returns the user's last night's sleep information.
     */
    SleepLogResponse getLastNightSleep(
            UUID userId
    );


    /**
     * Calculates the user's sleep averages over the last 30 days.
     */
    SleepSummaryResponse getLast30DaySummary(
            UUID userId
    );
}
