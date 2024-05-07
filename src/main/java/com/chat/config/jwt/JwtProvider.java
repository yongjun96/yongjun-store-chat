package com.chat.config.jwt;

import com.chat.config.common.exceptioncode.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    // 밀리세턴드 : 1초 == 1000L
    public static final long ACCESS_TIME = 3600000L;       // accessToken 1시간
    public static final long REFRESH_TIME = 2592000000L;   // refreshToken 30일

    //public static final long ACCESS_TIME = 3000L;
    //public static final long REFRESH_TIME = 6000L;

    // application custom secretKey
    @Value("${custom.jwt.secretKey}")
    private String secretKey;

    // secretKeyPlain Base64 변환
    public SecretKey jwtSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKey.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }


    // Request Header 에서 토큰 정보 추출
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // JWT 토큰 검증 및 파싱
    public String validateAndGetSubject(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


    // 유효성, 만료 일자 검증
    public boolean validateToken(String jwtToken) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecretKey()).build().parseClaimsJws(jwtToken);
            return true;
        }

        catch (SignatureException e) {
            log.info("SignatureException : 검증에 실패한 변조된 accessToken입니다.");
            throw new JwtException(ErrorCode.JWT_SIGNATURE_EXCEPTION.getMessage());
        }

        catch (MalformedJwtException e) {
            log.info("MalformedJwtException : 잘못된 구조의 지원되지 않는 accessToken입니다.");
            throw new JwtException(ErrorCode.JWT_MALFORMED_JWT_EXCEPTION.getMessage());
        }

        catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException : 만료된 accessToken입니다.");
            throw new JwtException(ErrorCode.JWT_EXPIRED_JWT_EXCEPTION.getMessage());
        }

        catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException : 원하는 accessToken과 다른 형식의 accessToken입니다.");
            throw new JwtException(ErrorCode.JWT_UNSUPPORTED_JWT_EXCEPTION.getMessage());
        }
    }


    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtSecretKey()).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

}

