package com.ssafy.realrealfinal.authms.api.auth.service;

import com.ssafy.realrealfinal.authms.api.auth.mapper.AuthMapper;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthTokenRes;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthUserRes;
import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.common.util.GithubUtil;
import com.ssafy.realrealfinal.authms.common.util.JwtUtil;
import com.ssafy.realrealfinal.authms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final GithubUtil githubUtil;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

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
            log.info("login start: code " + authorizeCode);
            OauthTokenRes jsonToken = githubUtil.getGithubOauthToken(authorizeCode);
            OauthUserRes oauthUserRes = githubUtil.getGithubUserInfo(jsonToken.getAccessToken());
            redisUtil.setDataWithExpire(oauthUserRes.getId().toString(), jsonToken.getAccessToken(),
                31536000L); //365days
            //TODO:로그인 처리하기 (유저로 보내서)
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
}
