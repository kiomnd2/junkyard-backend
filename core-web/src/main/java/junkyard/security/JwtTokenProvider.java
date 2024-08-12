package junkyard.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.refresh.secret}")
    private String refreshSecretKey;

    private Key accessKey;
    private Key refreshKey;

    @PostConstruct
    public void init() {
        accessKey = Keys.hmacShaKeyFor(secretKey.getBytes());
        refreshKey = Keys.hmacShaKeyFor(refreshSecretKey.getBytes());
    }


    public String createToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .signWith(new SecretKeySpec(secretKey.getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setSubject(String.valueOf(id))
                .setExpiration(new Date(now.getTime() + 30 * 60 * 1000L))
                .compact();
    }

    public String createRefreshToken(Long id) {
        Date now = new Date();
        return Jwts.builder()
                .signWith(new SecretKeySpec(refreshSecretKey.getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))
                .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
                .setExpiration(new Date(now.getTime() + 1000L * 60L * 60L * 24L * 30L)) // 31일 유효
                .setSubject(String.valueOf(id))
                .compact();
    }

    public String validToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(accessKey).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRefreshSubject(String refreshToken) {
        Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken);
        return claims.getBody().getSubject();
    }

    // 토큰 유효성, 만료일자 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(accessKey).build().parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 리프레시 토큰 유효성, 만료일자 확인
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(refreshKey).build().parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
