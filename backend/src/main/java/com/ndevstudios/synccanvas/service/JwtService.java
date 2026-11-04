package com.ndevstudios.synccanvas.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;

    public JwtService(@Value("${jwt.secret}") String secret){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String userId){
        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 15*60*1000))
                .signWith(signingKey)
                .compact();
    }

    public String extractUserId(String token){
       return Jwts.parser()
               .verifyWith(signingKey)
               .build()
               .parseSignedClaims(token)
               .getPayload()
               .getSubject();
    }

    public boolean isTokenValid(String token, String expectedUserId){
        try{
            String tokenUserId = extractUserId(token);
            return tokenUserId.equals(expectedUserId);
        }catch (JwtException | IllegalArgumentException exception){
            return false;
        }
    }
}
