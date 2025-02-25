package com.developer.onlybuns.service;

public interface LoginAttemptService {

     boolean isBlocked(String ipAddress);

     void recordAttempt(String ipAddress);
}
