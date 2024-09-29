package org.example.userservice.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

@NoArgsConstructor
@Getter
@Setter
@Service
public class JwtUtil {

    // Secret key for signing the JWT token
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // Token expiration time (e.g., 1 hour)
    @Value("${security.jwt.expiration-time}")
    private long jwtExpirationDuration;

    public String generateToken(Long userId) {
        Date expirationDate = new Date(System.currentTimeMillis() + jwtExpirationDuration);

        return Jwts.builder()
                .subject(String.valueOf(userId)) // Set the user identifier
//                .claim("customField", customFieldValue) // Add custom claims
                .issuedAt(new Date()) // Set the issued date
                .expiration(expirationDate) // Set the expiration date
                .signWith(SignatureAlgorithm.HS256, getSignInKey()) // Sign the token with the secret key
                .compact(); // Build and compact the token
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getBody();
    }


    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean isTokenValid(String token, Long userId) {
        final String tokenUserId = extractUserId(token);
        return (tokenUserId.equals(String.valueOf(userId))) && !isTokenExpired(token);
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
