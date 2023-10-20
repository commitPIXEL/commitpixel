package com.ssafy.realrealfinal.authms.api.auth.service;

import com.ssafy.realrealfinal.authms.api.auth.response.OauthTokenRes;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthUserRes;
import com.ssafy.realrealfinal.authms.common.util.GithubUtil;
import com.ssafy.realrealfinal.authms.common.util.JwtUtil;
import com.ssafy.realrealfinal.authms.common.util.RedisUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
//@Transactional
@Service
public class AuthServiceImpl implements AuthService {

    private final GithubUtil githubUtil;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    @Override
    public String login(String authorizeCode, String provider) {
        if(provider.equals("github")){
            log.info("login start. code: " + authorizeCode);
            OauthTokenRes jsonToken = githubUtil.getGithubOauthToken(authorizeCode);
            OauthUserRes oauthUserRes = githubUtil.getGithubUserInfo(jsonToken.getAccessToken());
            redisUtil.setDataWithExpire(oauthUserRes.getId().toString(),jsonToken.getAccessToken(),31536000L ); //365days
            //로그인 처리하기 (유저로 보내서)

        }
        return null;
    }
}
