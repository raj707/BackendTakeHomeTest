package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.Feeling;
import com.noom.interview.fullstack.sleep.entity.SleepLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Test class for SleepLogRepositoryImpl
 */
class SleepLogRepositoryImplTest {

    private NamedParameterJdbcTemplate jdbcTemplate;

    private SleepLogRepositoryImpl repository;


    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        repository = new SleepLogRepositoryImpl(jdbcTemplate);
    }


    @Test
    void shouldSaveSleepLog() {

        SleepLog sleepLog = createSleepLog();

        when(jdbcTemplate.update(
                anyString(),
                any(SqlParameterSource.class)
        )).thenReturn(1);


        SleepLog result = repository.save(sleepLog);


        assertThat(result)
                .isEqualTo(sleepLog);

        verify(jdbcTemplate)
                .update(
                        anyString(),
                        any(SqlParameterSource.class)
                );
    }


    @Test
    void shouldReturnLatestSleepLogForUser() {

        UUID userId = UUID.randomUUID();

        SleepLog expectedSleepLog = createSleepLog();
        expectedSleepLog.setUserId(userId);


        when(jdbcTemplate.query(
                anyString(),
                anyMap(),
                ArgumentMatchers.<RowMapper<SleepLog>>any()
        )).thenReturn(List.of(expectedSleepLog));


        Optional<SleepLog> result =
                repository.findMostRecentByUserId(userId);


        assertThat(result)
                .isPresent();

        assertThat(result.get())
                .isEqualTo(expectedSleepLog);


        verify(jdbcTemplate)
                .query(
                        anyString(),
                        anyMap(),
                        ArgumentMatchers.<RowMapper<SleepLog>>any()
                );
    }


    @Test
    void shouldReturnEmptyWhenNoSleepLogExistsForUser() {

        UUID userId = UUID.randomUUID();


        when(jdbcTemplate.query(
                anyString(),
                anyMap(),
                ArgumentMatchers.<RowMapper<SleepLog>>any()
        )).thenReturn(List.of());


        Optional<SleepLog> result =
                repository.findMostRecentByUserId(userId);


        assertThat(result)
                .isEmpty();
    }


    @Test
    void shouldReturnSleepLogsWithinDateRange() {

        UUID userId = UUID.randomUUID();

        LocalDate fromDate = LocalDate.now().minusDays(30);
        LocalDate toDate = LocalDate.now();


        SleepLog sleepLog = createSleepLog();
        sleepLog.setUserId(userId);


        when(jdbcTemplate.query(
                anyString(),
                anyMap(),
                ArgumentMatchers.<RowMapper<SleepLog>>any()
        )).thenReturn(List.of(sleepLog));


        List<SleepLog> result =
                repository.findByUserIdAndDateRange(
                        userId,
                        fromDate,
                        toDate
                );


        assertThat(result)
                .hasSize(1);

        assertThat(result.get(0))
                .isEqualTo(sleepLog);


        verify(jdbcTemplate)
                .query(
                        anyString(),
                        anyMap(),
                        ArgumentMatchers.<RowMapper<SleepLog>>any()
                );
    }


    @Test
    void shouldReturnEmptyListWhenNoSleepLogsExistInDateRange() {

        UUID userId = UUID.randomUUID();


        when(jdbcTemplate.query(
                anyString(),
                anyMap(),
                ArgumentMatchers.<RowMapper<SleepLog>>any()
        )).thenReturn(List.of());


        List<SleepLog> result =
                repository.findByUserIdAndDateRange(
                        userId,
                        LocalDate.now().minusDays(30),
                        LocalDate.now()
                );


        assertThat(result)
                .isEmpty();
    }


    private SleepLog createSleepLog() {

        return new SleepLog(
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now(),
                LocalDateTime.now().minusHours(8),
                LocalDateTime.now(),
                480,
                Feeling.GOOD,
                LocalDateTime.now()
        );
    }
}
