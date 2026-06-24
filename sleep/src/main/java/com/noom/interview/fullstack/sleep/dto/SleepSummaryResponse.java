package com.noom.interview.fullstack.sleep.dto;

import com.noom.interview.fullstack.sleep.entity.Feeling;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

/**
 * Response containing aggregated sleep information for a time period.
 *
 * @param fromDate start date of the summary range
 * @param toDate end date of the summary range
 * @param averageTotalMinutesInBed average time spent in bed in minutes
 * @param averageBedtime average time the user went to bed
 * @param averageWakeupTime average time the user woke up
 * @param feelingFrequency count of occurrences for each feeling category
 */
public record SleepSummaryResponse(
        LocalDate fromDate,
        LocalDate toDate,
        Integer averageTotalMinutesInBed,
        LocalTime averageBedtime,
        LocalTime averageWakeupTime,
        Map<Feeling, Integer> feelingFrequency
) {}
