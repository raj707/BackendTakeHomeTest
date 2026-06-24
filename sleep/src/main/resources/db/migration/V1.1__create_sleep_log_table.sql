CREATE TABLE sleep_log (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    sleep_date DATE NOT NULL,
    bedtime TIMESTAMP NOT NULL,
    wakeup_time TIMESTAMP NOT NULL,
    total_minutes_in_bed INTEGER NOT NULL,
    feeling VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
