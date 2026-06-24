package com.noom.interview.fullstack.sleep.mapper;

import com.noom.interview.fullstack.sleep.dto.SleepLogResponse;
import com.noom.interview.fullstack.sleep.entity.SleepLog;
import org.springframework.stereotype.Component;

/**
 * Mapper responsible for converting sleep log entities into API response DTOs.
 */
@Component
public class SleepLogMapper {

    /**
     * Converts a {@link SleepLog} entity into a {@link SleepLogResponse}.
     *
     * @param sleepLog entity retrieved from persistence layer
     * @return API response representation of the sleep log
     */
    public SleepLogResponse toResponse(SleepLog sleepLog) {

        return new SleepLogResponse(
                sleepLog.getId(),
                sleepLog.getSleepDate(),
                sleepLog.getBedtime(),
                sleepLog.getWakeupTime(),
                sleepLog.getTotalMinutesInBed(),
                sleepLog.getFeeling()
        );
    }
}
