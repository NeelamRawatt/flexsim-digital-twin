package com.postnord.simulation_service.security;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    

    @Value("${jwt.secret}")
    private String secret;


    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String extractUsername(String token)
    {
        Claims claims = Jwts.parser()
                        .verifyWith(getSigningKey())
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();
        return claims.getSubject();
    }

    public boolean isTokenValid(String token)
    {
        try{

            Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
