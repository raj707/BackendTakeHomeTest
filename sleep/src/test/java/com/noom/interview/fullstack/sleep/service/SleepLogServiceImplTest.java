package com.noom.interview.fullstack.sleep.service;

import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponse;
import com.noom.interview.fullstack.sleep.dto.SleepSummaryResponse;
import com.noom.interview.fullstack.sleep.entity.Feeling;
import com.noom.interview.fullstack.sleep.entity.SleepLog;
import com.noom.interview.fullstack.sleep.exception.SleepLogNotFoundException;
import com.noom.interview.fullstack.sleep.mapper.SleepLogMapper;
import com.noom.interview.fullstack.sleep.repository.SleepLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test class for SleepLogServiceImpl
 */
class SleepLogServiceImplTest {

    private SleepLogRepository sleepLogRepository;

    private SleepLogServiceImpl sleepLogService;


    @BeforeEach
    void setUp() {

        sleepLogRepository = mock(SleepLogRepository.class);
        SleepLogMapper sleepLogMapper = new SleepLogMapper();

        sleepLogService = new SleepLogServiceImpl(
                sleepLogRepository,
                sleepLogMapper
        );
    }


    @Test
    void shouldCreateSleepLogWithCalculatedDuration() {

        UUID userId = UUID.randomUUID();

        LocalDateTime bedtime =
                LocalDateTime.of(2026, 6, 22, 22, 30);

        LocalDateTime wakeupTime =
                LocalDateTime.of(2026, 6, 23, 6, 30);


        CreateSleepLogRequest request =
                new CreateSleepLogRequest(
                        bedtime,
                        wakeupTime,
                        Feeling.GOOD
                );


        when(sleepLogRepository.save(any(SleepLog.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );


        SleepLogResponse response =
                sleepLogService.createSleepLog(
                        userId,
                        request
                );


        assertThat(response.totalMinutesInBed())
                .isEqualTo(480);

        assertThat(response.feeling())
                .isEqualTo(Feeling.GOOD);


        verify(sleepLogRepository)
                .save(any(SleepLog.class));
    }


    @Test
    void shouldReturnLastNightSleepWhenSleepExistsForToday() {

        UUID userId = UUID.randomUUID();


        SleepLog sleepLog =
                new SleepLog(
                        UUID.randomUUID(),
                        userId,
                        LocalDate.now(),
                        LocalDateTime.now().minusHours(8),
                        LocalDateTime.now(),
                        480,
                        Feeling.GOOD,
                        LocalDateTime.now()
                );


        when(sleepLogRepository.findMostRecentByUserId(userId))
                .thenReturn(Optional.of(sleepLog));


        SleepLogResponse response =
                sleepLogService.getLastNightSleep(userId);


        assertThat(response.feeling())
                .isEqualTo(Feeling.GOOD);


        verify(sleepLogRepository)
                .findMostRecentByUserId(userId);
    }


    @Test
    void shouldThrowExceptionWhenNoSleepLogExists() {

        UUID userId = UUID.randomUUID();


        when(sleepLogRepository.findMostRecentByUserId(userId))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() ->
                sleepLogService.getLastNightSleep(userId)
        ).isInstanceOf(SleepLogNotFoundException.class);
    }


    @Test
    void shouldThrowExceptionWhenLatestSleepIsNotToday() {

        UUID userId = UUID.randomUUID();


        SleepLog oldSleepLog =
                new SleepLog(
                        UUID.randomUUID(),
                        userId,
                        LocalDate.now().minusDays(1),
                        LocalDateTime.now().minusHours(8),
                        LocalDateTime.now(),
                        480,
                        Feeling.GOOD,
                        LocalDateTime.now()
                );


        when(sleepLogRepository.findMostRecentByUserId(userId))
                .thenReturn(Optional.of(oldSleepLog));


        assertThatThrownBy(() ->
                sleepLogService.getLastNightSleep(userId)
        ).isInstanceOf(SleepLogNotFoundException.class);
    }

    @Test
    void shouldRejectSleepLogWhenWakeupTimeIsBeforeBedtime() {

        UUID userId = UUID.randomUUID();

        LocalDateTime bedtime =
                LocalDateTime.of(2026, 6, 23, 8, 0);

        LocalDateTime wakeupTime =
                LocalDateTime.of(2026, 6, 23, 6, 0);


        CreateSleepLogRequest request =
                new CreateSleepLogRequest(
                        bedtime,
                        wakeupTime,
                        Feeling.BAD
                );


        assertThatThrownBy(() ->
                sleepLogService.createSleepLog(
                        userId,
                        request
                )
        )
                .isInstanceOf(IllegalArgumentException.class);


        verify(sleepLogRepository, never())
                .save(any(SleepLog.class));
    }

    @Test
    void shouldCalculateTotalMinutesInBedWhenCreatingSleepLog() {

        UUID userId = UUID.randomUUID();

        LocalDateTime bedtime =
                LocalDateTime.of(2026, 6, 22, 22, 30);

        LocalDateTime wakeupTime =
                LocalDateTime.of(2026, 6, 23, 6, 45);


        CreateSleepLogRequest request =
                new CreateSleepLogRequest(
                        bedtime,
                        wakeupTime,
                        Feeling.GOOD
                );


        when(sleepLogRepository.save(any(SleepLog.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );


        SleepLogResponse response =
                sleepLogService.createSleepLog(
                        userId,
                        request
                );


        // 22:30 -> 06:45 = 8 hours 15 minutes
        assertThat(response.totalMinutesInBed())
                .isEqualTo(495);


        verify(sleepLogRepository)
                .save(any(SleepLog.class));
    }

    @Test
    void shouldReturnLast30DaySleepSummary() {

        UUID userId = UUID.randomUUID();

        LocalDate today = LocalDate.now();

        SleepLog log1 = new SleepLog(
                UUID.randomUUID(),
                userId,
                today.minusDays(1),
                LocalDateTime.of(
                        today.minusDays(1),
                        java.time.LocalTime.of(22, 0)
                ),
                LocalDateTime.of(
                        today,
                        java.time.LocalTime.of(6, 0)
                ),
                480,
                Feeling.GOOD,
                LocalDateTime.now()
        );

        SleepLog log2 = new SleepLog(
                UUID.randomUUID(),
                userId,
                today.minusDays(2),
                LocalDateTime.of(
                        today.minusDays(2),
                        java.time.LocalTime.of(23, 0)
                ),
                LocalDateTime.of(
                        today.minusDays(1),
                        java.time.LocalTime.of(7, 0)
                ),
                480,
                Feeling.OK,
                LocalDateTime.now()
        );


        when(sleepLogRepository.findByUserIdAndDateRange(
                userId,
                today.minusDays(29),
                today
        )).thenReturn(List.of(log1, log2));


        SleepSummaryResponse response =
                sleepLogService.getLast30DaySummary(userId);


        assertThat(response.fromDate())
                .isEqualTo(today.minusDays(29));

        assertThat(response.toDate())
                .isEqualTo(today);


        assertThat(response.averageTotalMinutesInBed())
                .isEqualTo(480);


        assertThat(response.feelingFrequency())
                .containsEntry(Feeling.GOOD, 1)
                .containsEntry(Feeling.OK, 1);
    }


    @Test
    void shouldReturnEmptySummaryWhenNoSleepLogsExist() {

        UUID userId = UUID.randomUUID();

        when(sleepLogRepository.findByUserIdAndDateRange(
                userId,
                LocalDate.now().minusDays(29),
                LocalDate.now()
        )).thenReturn(List.of());


        SleepSummaryResponse response =
                sleepLogService.getLast30DaySummary(userId);


        assertThat(response.averageTotalMinutesInBed())
                .isZero();

        assertThat(response.feelingFrequency())
                .isEmpty();
    }
}
