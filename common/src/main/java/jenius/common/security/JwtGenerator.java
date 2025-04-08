package jenius.common.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtGenerator {

    private final SecretKey key;

    public JwtGenerator(@Value("${jwt.secret-key}")
                        String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret); // secretKey 를 디코딩하여 바이트 배열로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes); // HMAC-SHA 알고리즘에 사용할 수 있는 비밀 키 객체 생성
    }

    public String generateAccessToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        Date accessTokenExpiryDate = new Date();
        accessTokenExpiryDate.setTime(accessTokenExpiryDate.getTime() + JwtConstants.ACCESS_TOKEN_EXPIRY_HOUR);

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(JwtConstants.AUTHORITY_CLAIMS_NAME, authorities)
                .expiration(accessTokenExpiryDate)
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        String authorities = getAuthorities(authentication);

        Date refreshTokenExpiryDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refreshTokenExpiryDate);
        calendar.add(Calendar.DAY_OF_MONTH, JwtConstants.REFRESH_TOKEN_EXPIRY_DAY);
        refreshTokenExpiryDate = calendar.getTime();

        return Jwts.builder()
                .subject(authentication.getName())
                .claim(JwtConstants.AUTHORITY_CLAIMS_NAME, authorities)
                .expiration(refreshTokenExpiryDate)
                .signWith(key)
                .compact();
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }


}
