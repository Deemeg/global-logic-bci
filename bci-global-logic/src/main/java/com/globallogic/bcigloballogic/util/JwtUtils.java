package com.globallogic.bcigloballogic.util;


import com.globallogic.bcigloballogic.configuration.EnvironmentConfig;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private final EnvironmentConfig environmentConfig;

    @Autowired
    public JwtUtils(EnvironmentConfig environmentConfig) {
        this.environmentConfig = environmentConfig;
    }


    public String generateToken(String email) {
         return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + environmentConfig.getExpiration()))
                .signWith(SignatureAlgorithm.HS256, environmentConfig.getSecret())
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(environmentConfig.getSecret()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect");
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(environmentConfig.getSecret()).build().parseClaimsJws(token).getBody().getSubject();
    }

}