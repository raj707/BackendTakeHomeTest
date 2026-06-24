package com.noom.interview.fullstack.sleep.dto;

import com.noom.interview.fullstack.sleep.entity.Feeling;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response representation of a user's sleep log.
 *
 * @param id unique identifier of the sleep log
 * @param sleepDate date associated with the sleep session
 * @param bedtime time when the user went to bed
 * @param wakeupTime time when the user woke up
 * @param totalMinutesInBed total duration spent in bed in minutes
 * @param feeling user's reported feeling after waking up
 */
public record SleepLogResponse(
        UUID id,
        LocalDate sleepDate,
        LocalDateTime bedtime,
        LocalDateTime wakeupTime,
        Integer totalMinutesInBed,
        Feeling feeling
) {}
