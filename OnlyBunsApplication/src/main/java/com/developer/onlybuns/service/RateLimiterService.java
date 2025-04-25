package com.developer.onlybuns.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service
public class RateLimiterService {
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_MILLIS = 60_000; // 1 minut

    // Čuva ID korisnika i listu vremena kad su poslati zahtevi
    private final ConcurrentHashMap<Integer, Deque<Long>> requestTimes = new ConcurrentHashMap<>();

    public boolean isAllowed(Integer userId) {
        long currentTime = Instant.now().toEpochMilli();

        // Dobavi listu ili napravi novu ako je nema
        requestTimes.putIfAbsent(userId, new ConcurrentLinkedDeque<>());
        Deque<Long> timestamps = requestTimes.get(userId);

        synchronized (timestamps) {
            // Ukloni sve zahteve koji su stariji od 1 minuta
            while (!timestamps.isEmpty() && currentTime - timestamps.peekFirst() > TIME_WINDOW_MILLIS) {
                timestamps.pollFirst();
            }

            if (timestamps.size() < MAX_REQUESTS) {
                timestamps.addLast(currentTime);
                return true;
            } else {
                return false;
            }
        }
    }





}
