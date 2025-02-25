package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.service.LoginAttemptService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final int MAX_ATTEMPTS = 5;
    private final int TIME_WINDOW_MINUTES = 1;

    //Cuva pokusaje logovanja po IP adresi u mapi
    private final ConcurrentHashMap<String, List<LocalDateTime>> attempts = new ConcurrentHashMap<>();

    public boolean isBlocked(String ipAddress) {
        List<LocalDateTime> timestamps = attempts.getOrDefault(ipAddress, List.of());

        // Filtriraj pokušaje koji su u poslednjem minutu
        List<LocalDateTime> recentAttempts = timestamps.stream()
                .filter(t -> t.isAfter(LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES)))
                .collect(Collectors.toList());

        return recentAttempts.size() >= MAX_ATTEMPTS;
    }

    public void recordAttempt(String ipAddress) {
        attempts.merge(ipAddress, List.of(LocalDateTime.now()), (oldList, newList) -> {
            List<LocalDateTime> updatedList = oldList.stream()
                    .filter(t -> t.isAfter(LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES))) // Održavamo samo poslednje minute
                    .collect(Collectors.toList());

            updatedList.addAll(newList);
            return updatedList;
        });
    }
}
