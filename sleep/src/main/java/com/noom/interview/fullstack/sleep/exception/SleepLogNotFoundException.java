package com.noom.interview.fullstack.sleep.exception;

import java.util.UUID;

/**
 *  Creates an exception indicating that no sleep log was found for the specified user.
 */
public class SleepLogNotFoundException extends RuntimeException {

    public SleepLogNotFoundException(UUID userId) {
        super("No sleep log found for user: " + userId);
    }
}
