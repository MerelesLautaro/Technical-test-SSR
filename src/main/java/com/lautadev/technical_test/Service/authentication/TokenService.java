package com.lautadev.technical_test.Service.authentication;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface TokenService {
    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    long getExpirationTime();

    boolean isTokenValid(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    String generateRefreshToken(UserDetails userDetails);

    boolean isRefreshToken(String token);

    boolean isRefreshTokenValid(String token, UserDetails userDetails);

    String refreshAccessToken(String refreshToken, UserDetails userDetails);
}