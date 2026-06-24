package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.SleepLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing user sleep log persistence.
 */
public interface SleepLogRepository {

    /**
     * Saves a sleep log record.
     *
     * @param sleepLog sleep log data to persist
     * @return the persisted sleep log
     */
    SleepLog save(SleepLog sleepLog);

    /**
     * Retrieves the most recent sleep log for a user.
     *
     * @param userId identifier of the user
     * @return latest sleep log if one exists, otherwise empty
     */
    Optional<SleepLog> findMostRecentByUserId(UUID userId);

    /**
     * Retrieves sleep logs created within the given date range for a user.
     *
     * @param userId identifier of the user
     * @param fromDate start date of the search range inclusive
     * @param toDate end date of the search range inclusive
     * @return list of sleep logs matching the criteria
     */
    List<SleepLog> findByUserIdAndDateRange(
            UUID userId,
            LocalDate fromDate,
            LocalDate toDate);
}
