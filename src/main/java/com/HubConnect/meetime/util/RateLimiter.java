package com.HubConnect.meetime.util;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class RateLimiter {

    private final int MAX_REQUESTS = 10;
    private final long TIME_WINDOW_MILLIS = 60 * 1000;

    private final Queue<Long> requestTimestamps = new LinkedList<>();

    public synchronized boolean allowRequest() {
        long now = Instant.now().toEpochMilli();

        while (!requestTimestamps.isEmpty() && (now - requestTimestamps.peek()) > TIME_WINDOW_MILLIS) {
            requestTimestamps.poll();
        }

        if (requestTimestamps.size() < MAX_REQUESTS) {
            requestTimestamps.add(now);
            return true;
        }

        return false;
    }
}