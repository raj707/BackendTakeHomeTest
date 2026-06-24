package com.noom.interview.fullstack.sleep.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.noom.interview.fullstack.sleep.dto.CreateSleepLogRequest;
import com.noom.interview.fullstack.sleep.dto.SleepLogResponse;
import com.noom.interview.fullstack.sleep.dto.SleepSummaryResponse;
import com.noom.interview.fullstack.sleep.exception.GlobalExceptionHandler;
import com.noom.interview.fullstack.sleep.exception.SleepLogNotFoundException;
import com.noom.interview.fullstack.sleep.entity.Feeling;
import com.noom.interview.fullstack.sleep.service.SleepLogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test for SleepLogController
 */
@WebMvcTest(SleepLogController.class)
@Import(GlobalExceptionHandler.class)
class SleepLogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SleepLogService sleepLogService;

    @Test
    void shouldCreateSleepLog() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID sleepLogId = UUID.randomUUID();

        CreateSleepLogRequest request =
                new CreateSleepLogRequest(
                        LocalDateTime.of(
                                2026,
                                6,
                                23,
                                22,
                                30
                        ),
                        LocalDateTime.of(
                                2026,
                                6,
                                24,
                                6,
                                30
                        ),
                        Feeling.GOOD
                );

        SleepLogResponse response =
                new SleepLogResponse(
                        sleepLogId,
                        LocalDate.of(2026, 6, 24),
                        request.bedtime(),
                        request.wakeupTime(),
                        480,
                        Feeling.GOOD
                );

        when(
                sleepLogService.createSleepLog(
                        userId,
                        request
                )
        ).thenReturn(response);

        mockMvc.perform(
                        post("/users/{userId}/sleep-logs", userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.totalMinutesInBed")
                                .value(480)
                )
                .andExpect(
                        jsonPath("$.feeling")
                                .value("GOOD")
                );
    }

    @Test
    void shouldReturnLatestSleepLog() throws Exception {

        UUID userId = UUID.randomUUID();
        UUID sleepLogId = UUID.randomUUID();

        SleepLogResponse response =
                new SleepLogResponse(
                        sleepLogId,
                        LocalDate.of(2026, 6, 24),
                        LocalDateTime.of(
                                2026,
                                6,
                                23,
                                22,
                                30
                        ),
                        LocalDateTime.of(
                                2026,
                                6,
                                24,
                                6,
                                30
                        ),
                        480,
                        Feeling.GOOD
                );

        when(
                sleepLogService.getLastNightSleep(
                        userId
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get(
                                "/users/{userId}/sleep-logs/latest",
                                userId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.totalMinutesInBed")
                                .value(480)
                )
                .andExpect(
                        jsonPath("$.feeling")
                                .value("GOOD")
                );
    }

    @Test
    void shouldReturnSleepSummary() throws Exception {

        UUID userId = UUID.randomUUID();

        SleepSummaryResponse response =
                new SleepSummaryResponse(
                        LocalDate.of(2026, 5, 26),
                        LocalDate.of(2026, 6, 24),
                        480,
                        LocalTime.of(22, 30),
                        LocalTime.of(6, 30),
                        Map.of(
                                Feeling.GOOD, 20,
                                Feeling.OK, 8,
                                Feeling.BAD, 2
                        )
                );

        when(
                sleepLogService.getLast30DaySummary(
                        userId
                )
        ).thenReturn(response);

        mockMvc.perform(
                        get(
                                "/users/{userId}/sleep-logs/summary",
                                userId
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.averageTotalMinutesInBed")
                                .value(480)
                )
                .andExpect(
                        jsonPath("$.feelingFrequency.GOOD")
                                .value(20)
                )
                .andExpect(
                        jsonPath("$.feelingFrequency.OK")
                                .value(8)
                )
                .andExpect(
                        jsonPath("$.feelingFrequency.BAD")
                                .value(2)
                );
    }

    @Test
    void shouldReturnNotFoundWhenSleepLogDoesNotExist()
            throws Exception {

        UUID userId = UUID.randomUUID();

        when(
                sleepLogService.getLastNightSleep(
                        userId
                )
        ).thenThrow(
                new SleepLogNotFoundException(
                        userId)
        );

        mockMvc.perform(
                        get(
                                "/users/{userId}/sleep-logs/latest",
                                userId
                        )
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value("No sleep log found for user: " + userId)
                );
    }
}
