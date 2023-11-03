package com.ssafy.realrealfinal.authms.api.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthUserRes;
import com.ssafy.realrealfinal.authms.api.auth.mapper.AuthMapper;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthTokenRes;
import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.common.exception.auth.JsonifyException;
import com.ssafy.realrealfinal.authms.common.util.GithubUtil;
import com.ssafy.realrealfinal.authms.common.util.JwtUtil;
import com.ssafy.realrealfinal.authms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final GithubUtil githubUtil;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * jwt,redis 접근, 로그인 후 토큰 발급 메서드
     *
     * @param authorizeCode 인가코드
     * @param provider      깃허브
     * @return jwt의 accesstoken과 refreshtoken
     */
    @Override
    public TokenRes login(String authorizeCode, String provider) {
        if (provider.equals("github")) {
            log.info("login start: " + authorizeCode);
            OauthTokenRes jsonToken = githubUtil.getGithubOauthToken(authorizeCode);
            OauthUserRes oauthUserRes = githubUtil.getGithubUserInfo(jsonToken.getAccessToken());
            redisUtil.setDataWithExpire(oauthUserRes.getId().toString(), jsonToken.getAccessToken(),
                31536000L); //365days
            // OauthUserRes 객체를 JSON 문자열로 변환
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(oauthUserRes);
                // 변환된 JSON 문자열을 Kafka topic에 보냄
                kafkaTemplate.send("login-topic", jsonMessage);
                log.info("login mid: kafka json data: " + jsonMessage);
            } catch (JsonProcessingException e) {
                throw new JsonifyException();
            }

            String jwtRefreshToken = jwtUtil.createRefreshToken();
            String jwtAccessToken = jwtUtil.createAccessToken(jwtRefreshToken);
            saveTokens(oauthUserRes.getId().toString(), jwtRefreshToken,
                jsonToken.getAccessToken());
            TokenRes tokenRes = AuthMapper.INSTANCE.toTokenRes(jwtAccessToken, jwtRefreshToken);
            log.info("login end: " + tokenRes);
            return tokenRes;
        } else {
            return null;
        }
    }

    /**
     * 생성된 토큰 레디스에 저장
     *
     * @param providerId       providerId
     * @param refreshJWTToken  JWT refresh token
     * @param oauthAccessToken oauth access token
     */
    @Override
    public void saveTokens(String providerId, String refreshJWTToken, String oauthAccessToken) {
        log.info("saveTokens start: " + providerId + " " + refreshJWTToken + " "
            + oauthAccessToken);

        redisUtil.setDataWithExpire(refreshJWTToken, providerId, 1209600000L);
        redisUtil.setDataWithExpire(providerId, oauthAccessToken, 31536000L);

        log.info("saveTokens end: success");
    }

    /**
     * 로그아웃. 토큰 삭제 만약 Redis에서 단순한 삭제 작업만 수행하고 있고, 다른 데이터베이스 작업과 연계되지 않는 경우, @Transactional 어노테이션을
     * 사용할 필요는 없을 수 있습니다.
     *
     * @param refreshToken refreshToken
     */
    @Override
    public void logout(String refreshToken) {
        log.info("logout start" + refreshToken);
        redisUtil.deleteData(refreshToken);
        log.info("logout end: success");
    }

    /**
     * MS 통신을 위해 외부 MS에서 요청이 들어올 때 토큰에서 providerId를 추출해서 리턴하는 역할
     *
     * @param accessToken decode 위한 JWT 엑세스 토큰
     * @return providerId
     */
    @Override
    public Integer getProviderIDFromAccessToken(String accessToken) {
        log.info("getProviderIDFromAccessToken start: " + accessToken);
        Integer providerId = jwtUtil.getProviderIdFromToken(accessToken);
        log.info("getProviderIDFromAccessToken end: " + providerId);
        return providerId;
    }

    /**
     * providerId로 githubAccessToken을 redis에서 빼서 리턴해주기
     *
     * @param providerId github providerId
     * @return githubAccessToken
     */
    @Override
    public String getGithubAccessTokenByProviderId(String providerId) {
        log.info("getGithubAccessTokenByProviderId start: " + providerId);
        String githubAccessToken = redisUtil.getData(providerId);
        log.info("getGithubAccessTokenByProviderId end: " + githubAccessToken);
        return githubAccessToken;
    }
}
