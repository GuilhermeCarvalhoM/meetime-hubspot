package com.HubConnect.meetime.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class TokenService {

    private final AtomicReference<String> accessTokenRef = new AtomicReference<>();

    public void saveToken(String token) {
        accessTokenRef.set(token);
    }

    public String getToken() {
        return accessTokenRef.get();
    }

    public boolean hasToken() {
        return accessTokenRef.get() != null;
    }
}