package com.sharediary.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@RequiredArgsConstructor
@Component
public class JwtProvider {
    private String secret = "JooksopSuperSecretKeyForJWTGeneration!!"; // 32자 이상
    private SecretKey secretKey;

    private final long validityInMilliseconds = 3600000; // 1시간

    private final com.sharediary.user.repository.UserRepository userRepository;

    @PostConstruct
    protected void init() {
        this.secretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    public void invalidateToken(String token){
        System.out.println("🧹 토큰 무효화됨: " + token);
    }

    public String createToken(String userId, String nickname) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("nickname", nickname);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("❌ Token validation 실패: " + e.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getNickname(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("nickname");
    }

    public UserDetails getUserDetails(String userId){
        com.sharediary.user.domain.User user=userRepository.findByUserId(userId)
                .orElseThrow(()-> new RuntimeException("사용자를 찾을 수 없습니다. "+userId));

        System.out.println("🔎 userId로 유저 조회 시도: " + userId);

        return User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")))
                .build();
    }

    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);
            return token;
        }
        return null;
    }

}
