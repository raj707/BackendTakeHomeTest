package com.noom.interview.fullstack.sleep.repository;

import com.noom.interview.fullstack.sleep.entity.Feeling;
import com.noom.interview.fullstack.sleep.entity.SleepLog;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * JDBC implementation of {@link SleepLogRepository}.
 * Uses {@code NamedParameterJdbcTemplate} to execute SQL queries against the sleep log database table.
 */
@Repository
public class SleepLogRepositoryImpl implements SleepLogRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public SleepLogRepositoryImpl(
            NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public SleepLog save(SleepLog sleepLog) {
        String sql = """
                INSERT INTO sleep_log (
                    id,
                    user_id,
                    sleep_date,
                    bedtime,
                    wakeup_time,
                    total_minutes_in_bed,
                    feeling,
                    created_at
                )
                VALUES (
                    :id,
                    :userId,
                    :sleepDate,
                    :bedtime,
                    :wakeupTime,
                    :totalMinutesInBed,
                    :feeling,
                    :createdAt
                )
                """;

        jdbcTemplate.update(
                sql,
                new BeanPropertySqlParameterSource(sleepLog)
        );

        return sleepLog;
    }

    @Override
    public Optional<SleepLog> findMostRecentByUserId(UUID userId) {
        String sql = """
                SELECT *
                FROM sleep_log
                WHERE user_id = :userId
                ORDER BY sleep_date DESC
                LIMIT 1
                """;

        List<SleepLog> result = jdbcTemplate.query(
                sql,
                Map.of("userId", userId),
                this::mapRow
        );

        return result.stream().findFirst();
    }

    @Override
    public List<SleepLog> findByUserIdAndDateRange(UUID userId, LocalDate fromDate, LocalDate toDate) {
        String sql = """
                SELECT *
                FROM sleep_log
                WHERE user_id = :userId
                AND sleep_date BETWEEN :fromDate AND :toDate
                ORDER BY sleep_date DESC
                """;

        Map<String, Object> params = Map.of(
                "userId", userId,
                "fromDate", fromDate,
                "toDate", toDate
        );

        return jdbcTemplate.query(
                sql,
                params,
                this::mapRow
        );
    }

    /**
     * Maps a database row into a {@link SleepLog} entity.
     * This method converts database-specific types such as UUID, timestamps, and feeling values into their corresponding Java types
     * @param resultSet database result row
     * @param rowNum row number in the result set
     * @return populated sleep log entity
     * @throws SQLException if a database column cannot be read
     */
    private SleepLog mapRow(ResultSet resultSet, int rowNum)
            throws SQLException {

        return new SleepLog(
                resultSet.getObject("id", UUID.class),
                resultSet.getObject("user_id", UUID.class),
                resultSet.getDate("sleep_date").toLocalDate(),
                resultSet.getTimestamp("bedtime").toLocalDateTime(),
                resultSet.getTimestamp("wakeup_time").toLocalDateTime(),
                resultSet.getInt("total_minutes_in_bed"),
                Feeling.valueOf(resultSet.getString("feeling")),
                resultSet.getTimestamp("created_at").toLocalDateTime()
        );
    }
}
