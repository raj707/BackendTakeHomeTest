package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponse;
import com.noom.interview.fullstack.sleep.dto.SleepSummaryResponse;
import com.noom.interview.fullstack.sleep.entity.Feeling;
import com.noom.interview.fullstack.sleep.entity.SleepLog;
import com.noom.interview.fullstack.sleep.exception.SleepLogNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.SleepLogMapper;
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link SleepLogService}.
 */
@Service
public class SleepLogServiceImpl implements SleepLogService {

    private final SleepLogRepository sleepLogRepository;
    private final SleepLogMapper sleepLogMapper;


    public SleepLogServiceImpl(
            SleepLogRepository sleepLogRepository,
            SleepLogMapper sleepLogMapper) {

        this.sleepLogRepository = sleepLogRepository;
        this.sleepLogMapper = sleepLogMapper;
    }


    @Override
    public SleepLogResponse createSleepLog(
            UUID userId,
            CreateSleepLogRequest request) {

        int totalMinutesInBed =
                calculateTotalMinutes(
                        request.bedtime(),
                        request.wakeupTime()
                );


        SleepLog sleepLog = new SleepLog(
                userId,
                UUID.randomUUID(),
                LocalDate.now(),
                request.bedtime(),
                request.wakeupTime(),
                totalMinutesInBed,
                request.feeling(),
                java.time.LocalDateTime.now()
        );


        SleepLog saved =
                sleepLogRepository.save(sleepLog);


        return sleepLogMapper.toResponse(saved);
    }


    @Override
    public SleepLogResponse getLastNightSleep(UUID userId) {

        SleepLog sleepLog =
                sleepLogRepository.findMostRecentByUserId(userId)
                        .orElseThrow(() ->
                                new SleepLogNotFoundException(userId)
                        );


        if (!sleepLog.getSleepDate()
                .equals(LocalDate.now())) {

            throw new SleepLogNotFoundException(userId);
        }


        return sleepLogMapper.toResponse(sleepLog);
    }

    @Override
    public SleepSummaryResponse getLast30DaySummary(UUID userId) {
        // The summary includes sleep logs from today and the previous 29 days,
        // resulting in a rolling 30-day window.
        LocalDate toDate = LocalDate.now();
        LocalDate fromDate = toDate.minusDays(29);

        List<SleepLog> sleepLogs =
                sleepLogRepository.findByUserIdAndDateRange(
                        userId,
                        fromDate,
                        toDate
                );

        // Return an empty summary when the user has no sleep records
        // in the requested period. This keeps the API response predictable
        // and avoids returning null values.
        if (sleepLogs.isEmpty()) {
            return new SleepSummaryResponse(
                    fromDate,
                    toDate,
                    0,
                    null,
                    null,
                    Map.of()
            );
        }

        int averageTotalMinutesInBed =
                calculateAverageTotalMinutes(sleepLogs);

        LocalTime averageBedtime =
                calculateAverageTime(
                        sleepLogs,
                        SleepLog::getBedtime
                );

        LocalTime averageWakeupTime =
                calculateAverageTime(
                        sleepLogs,
                        SleepLog::getWakeupTime
                );

        Map<Feeling, Integer> feelingFrequency =
                calculateFeelingFrequency(sleepLogs);


        return new SleepSummaryResponse(
                fromDate,
                toDate,
                averageTotalMinutesInBed,
                averageBedtime,
                averageWakeupTime,
                feelingFrequency
        );
    }

    /**
     * Calculates the average duration spent in bed across sleep logs.
     */
    private int calculateAverageTotalMinutes(
            List<SleepLog> sleepLogs) {

        return (int) sleepLogs.stream()
                .mapToInt(SleepLog::getTotalMinutesInBed)
                .average()
                .orElse(0);
    }

    /**
     * Calculates the average clock time from sleep log timestamps.
     *
     * The calculation converts the time into minutes since midnight,
     * calculates the average, and converts it back into a LocalTime.
     * Note: A production implementation may need circular time averaging
     * to handle sleep times crossing midnight (for example, 23:30 and 00:30).
     */
    private LocalTime calculateAverageTime(
            List<SleepLog> sleepLogs,
            Function<SleepLog, LocalDateTime> extractor) {


        int averageMinutes =
                (int) sleepLogs.stream()
                        .map(extractor)
                        .map(LocalDateTime::toLocalTime)
                        .mapToInt(time ->
                                time.getHour() * 60
                                        + time.getMinute()
                        )
                        .average()
                        .orElse(0);


        return LocalTime.of(
                averageMinutes / 60,
                averageMinutes % 60
        );
    }

    /**
     * Calculates the number of occurrences for each feeling type.
     */
    private Map<Feeling, Integer> calculateFeelingFrequency(
            List<SleepLog> sleepLogs) {

        return sleepLogs.stream()
                .collect(Collectors.groupingBy(
                        SleepLog::getFeeling,
                        Collectors.collectingAndThen(
                                Collectors.counting(),
                                Long::intValue
                        )
                ));
    }

    /**
     * Calculates the total time spent in bed between bedtime and wake-up time.
     *
     * @param bedtime time when the user went to bed
     * @param wakeupTime time when the user woke up
     * @return total number of minutes spent in bed
     */
    private int calculateTotalMinutes(
            java.time.LocalDateTime bedtime,
            java.time.LocalDateTime wakeupTime) {

        if (wakeupTime.isBefore(bedtime)) {
            throw new IllegalArgumentException(
                    "Wake up time cannot be before bedtime"
            );
        }

        return (int) Duration
                .between(bedtime, wakeupTime)
                .toMinutes();
    }
}
