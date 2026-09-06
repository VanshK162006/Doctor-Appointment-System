package in.app.doctorAppointmentSystem.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// Fixed: this class was a plain object before - Spring could never inject it
// anywhere (e.g. into JwtAuthFilter) without @Component.
@Component
public class JwtServices {

    @Value("${application.jwt.key}")
    private String key;

    @Value("${application.jwt.expiry}")
    private long expiryMillis;

    public String generateToken(UserDetails userDetails) {
        Set<String> authorities = userDetails.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toSet());

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiryMillis))
                .signWith(getKey())
                .compact();
    }

    // Fixed: previously read the CURRENT SecurityContext's Authentication to
    // compare against - but this method is called by JwtAuthFilter BEFORE any
    // authentication exists for the request. That guaranteed a NullPointerException
    // on every request. Now both token and userDetails are passed in explicitly.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = extractClaims(token);
            return claims.getSubject().equals(userDetails.getUsername())
                    && claims.getExpiration().after(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            // Malformed, expired (via parsing), or tampered token - treat as invalid,
            // not as a crash.
            return false;
        }
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {
        return io.jsonwebtoken.security.Keys.hmacShaKeyFor(key.getBytes());
    }
}
