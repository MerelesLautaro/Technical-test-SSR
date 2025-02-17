package com.lautadev.technical_test.Security;

import com.lautadev.technical_test.Service.authentication.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class JwtBlacklistManager {

    private final TokenService tokenService;
    private final Set<String> tokenBlacklist;

    public JwtBlacklistManager(TokenService tokenService) {
        this.tokenService = tokenService;
        tokenBlacklist = ConcurrentHashMap.newKeySet();
    }

    @Scheduled(fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void cleanBlackList() {
        log.info("Deleting expired tokens from black list");

        tokenBlacklist.stream()
                .filter(tokenService::isTokenExpired)
                .forEach(tokenBlacklist::remove);
    }

    public void addTokenToBlackList(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isBlackListed(String token) {
        return tokenBlacklist.contains(token);
    }
}

