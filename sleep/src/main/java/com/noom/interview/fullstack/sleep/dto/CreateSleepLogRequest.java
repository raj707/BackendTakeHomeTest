package com.noom.interview.fullstack.sleep.dto;

import com.noom.interview.fullstack.sleep.entity.Feeling;

import java.time.LocalDateTime;

/**
 * Request payload used to create a user's sleep log.
 *
 * @param bedtime time when the user went to bed
 * @param wakeupTime time when the user woke up
 * @param feeling user's morning feeling after sleep
 */
public record CreateSleepLogRequest(
        LocalDateTime bedtime,
        LocalDateTime wakeupTime,
        Feeling feeling
) {}
