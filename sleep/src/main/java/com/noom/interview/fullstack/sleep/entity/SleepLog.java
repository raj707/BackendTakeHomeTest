package com.noom.interview.fullstack.sleep.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a user's sleep record for a single night.
 * Note: The total time in bed is stored in minutes to simplify aggregation queries such as 30-day sleep averages.
 */
public class SleepLog {

    private UUID id;

    private UUID userId;

    private LocalDate sleepDate;

    private LocalDateTime bedtime;

    private LocalDateTime wakeupTime;

    private Integer totalMinutesInBed;

    private Feeling feeling;

    private LocalDateTime createdAt;

    public SleepLog() {
    }

    public SleepLog(UUID userId, UUID id, LocalDate sleepDate, LocalDateTime bedtime, LocalDateTime wakeupTime, Integer totalMinutesInBed, Feeling feeling, LocalDateTime createdAt) {
        this.userId = userId;
        this.id = id;
        this.sleepDate = sleepDate;
        this.bedtime = bedtime;
        this.wakeupTime = wakeupTime;
        this.totalMinutesInBed = totalMinutesInBed;
        this.feeling = feeling;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDate getSleepDate() {
        return sleepDate;
    }

    public void setSleepDate(LocalDate sleepDate) {
        this.sleepDate = sleepDate;
    }

    public LocalDateTime getBedtime() {
        return bedtime;
    }

    public void setBedtime(LocalDateTime bedtime) {
        this.bedtime = bedtime;
    }

    public LocalDateTime getWakeupTime() {
        return wakeupTime;
    }

    public void setWakeupTime(LocalDateTime wakeupTime) {
        this.wakeupTime = wakeupTime;
    }

    public Integer getTotalMinutesInBed() {
        return totalMinutesInBed;
    }

    public void setTotalMinutesInBed(Integer totalMinutesInBed) {
        this.totalMinutesInBed = totalMinutesInBed;
    }

    public Feeling getFeeling() {
        return feeling;
    }

    public void setFeeling(Feeling feeling) {
        this.feeling = feeling;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SleepLog{" +
                "id=" + id +
                ", userId=" + userId +
                ", sleepDate=" + sleepDate +
                ", bedtime=" + bedtime +
                ", wakeupTime=" + wakeupTime +
                ", totalMinutesInBed=" + totalMinutesInBed +
                ", feeling=" + feeling +
                ", createdAt=" + createdAt +
                '}';
    }
}
