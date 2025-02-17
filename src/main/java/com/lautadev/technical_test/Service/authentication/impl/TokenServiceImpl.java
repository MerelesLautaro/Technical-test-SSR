package com.lautadev.technical_test.Service.authentication.impl;

import com.lautadev.technical_test.Service.authentication.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;


    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, expiration,"access");
    }

    @Override
    public long getExpirationTime() {
        return expiration;
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration,
            String tokenType
    ) {
        extraClaims.put("token_type", tokenType);
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration,"refresh");
    }

    @Override
    public boolean isRefreshToken(String token) {
        String tokenType = extractClaim(token, claims -> (String) claims.get("token_type"));
        return "refresh".equals(tokenType);
    }

    @Override
    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {
        return !isTokenExpired(token);
    }

    @Override
    public String refreshAccessToken(String refreshToken, UserDetails userDetails) {
        if (isRefreshTokenValid(refreshToken, userDetails)) {
            return generateToken(userDetails);
        }
        throw new SecurityException("Invalid refresh token");
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}

