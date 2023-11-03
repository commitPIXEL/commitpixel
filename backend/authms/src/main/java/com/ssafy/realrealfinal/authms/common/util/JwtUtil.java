package com.ssafy.realrealfinal.authms.common.util;

import com.ssafy.realrealfinal.authms.common.exception.auth.CreateTokenException;
import com.ssafy.realrealfinal.authms.common.exception.auth.ExpiredTokenException;
import com.ssafy.realrealfinal.authms.common.exception.auth.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtUtil {

    //TODO: 기간 설정 다시 확인하기.
    @Value("${jwt.access-token.expire-length}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token.expire-length}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    private final RedisUtil redisUtil;

    /**
     * access token 생성
     *
     * @param  providerId Integer
     * @return 생성된 token
     */
    public String createAccessToken(Integer providerId) {
        log.info("createAccessToken start: " + providerId);
        String token = createToken(String.valueOf(providerId), accessTokenValidityInMilliseconds);
        log.info("createAccessToken end: " + token);
        return token;
    }

    /**
     * refresh token 생성
     *
     * @return 생성된 refresh token
     */
    public String createRefreshToken() {
        log.info("createRefreshToken start");
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String generatedString = new String(array, StandardCharsets.UTF_8);
        String token = createToken(generatedString, refreshTokenValidityInMilliseconds);
        log.info("createRefreshToken end: " + token);
        return token;
    }

    /**
     * 토큰 생성. (access, refresh 토큰 생성시 사용하기 위한 메서드)
     *
     * @param payload      토큰에 저장할 정보. providerId 또는 랜덤 문자열 (refreshtoken 생성용)
     * @param expireLength 만료 기간
     * @return 만들어진 토큰
     */
    public String createToken(String payload, long expireLength) {
        log.info("createToken start: " + payload + " " + expireLength);
        try {
            Claims claims = Jwts.claims().setSubject(payload);
            Date now = new Date();
            Date validity = new Date(now.getTime() + expireLength);

            byte[] apiKeySecretBytes = secretKey.getBytes(); // 문자열을 바이트 배열로 변환
            Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                SignatureAlgorithm.HS256.getJcaName()); // Key로 변환

            String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(signingKey) // 변환된 Key를 사용
                .compact();

            log.info("createToken end: " + token);
            return token;
        } catch (Exception e) {
            throw new CreateTokenException();
        }
    }

    /**
     * 토큰에 담긴 정보 추출 메서드 payload의 sub에 userId가 담겨있으므로 추출
     *
     * @param accessToken 정보 추출할 토큰. (우리는 refresh token에 정보를 담지 않아 access만 가능하다)
     * @return providerId
     */
    public Integer getProviderIdFromToken(String accessToken) {
        log.info("getUserIdFromToken start: " + accessToken);
        try {
            byte[] apiKeySecretBytes = secretKey.getBytes();
            Key signingKey = new SecretKeySpec(apiKeySecretBytes,
                SignatureAlgorithm.HS256.getJcaName());
            Integer providerId = Integer.parseInt(Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject());
            log.info("getUserIdFromToken end: " + providerId);
            return providerId;
        } catch (ExpiredJwtException e) {
            Integer providerId = Integer.parseInt(e.getClaims().getSubject());
            log.info("getUserIdFromToken end: " + providerId);
            return providerId;
        } catch (JwtException e) {
            throw new ExpiredTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }
}
