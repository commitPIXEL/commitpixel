package com.ssafy.realrealfinal.authms.common.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthUserRes;
import com.ssafy.realrealfinal.authms.api.auth.response.OauthTokenRes;
import com.ssafy.realrealfinal.authms.common.exception.auth.GithubException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GithubUtil {

    @Value("${spring.github.oauth2.token-uri}")
    private String TOKEN_URL;

    @Value("${spring.github.oauth2.client-id}")
    private String clientId;

    @Value("${spring.github.oauth2.client-secret}")
    private String clientSecret;

    @Value("${spring.github.oauth2.redirect-uri}")
    private String redirectUri;

    @Value("${spring.github.oauth2.user-info-uri}")
    private String userInfoUri;

    /**
     * 인가코드로 깃허브의 accesstoken 발급받기
     *
     * @param authorizeCode 인가코드
     * @return 토큰 정보
     */
    public OauthTokenRes getGithubOauthToken(String authorizeCode) {
        log.info("getGithubOauthToken start code: " + authorizeCode);
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(TOKEN_URL);
        OauthTokenRes tokenResponse = null;
        try {
            List<BasicNameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("code", authorizeCode));
            params.add(new BasicNameValuePair("redirect_uri", redirectUri));

            post.setEntity(new UrlEncodedFormEntity(params));
            post.setHeader("Accept", "application/json");
            log.info("getGithubOauthToken mid : " + post);

            HttpResponse response = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            tokenResponse = mapper.readValue(response.getEntity().getContent(),
                OauthTokenRes.class);
            log.info("getGithubOauthToken end : " + tokenResponse);
        } catch (IOException e) {
            throw new GithubException();
        }
        return tokenResponse;

    }

    /**
     * 깃허브 엑세스 토큰으로 깃허브의 유저 정보 요청하기 api 호출하기
     *
     * @param oauthToken 깃허브 엑세스 토큰
     * @return 깃허브의 유저정보
     */
    public OauthUserRes getGithubUserInfo(String oauthToken) {
        log.info("getGithubUserInfo start: " + oauthToken);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        OauthUserRes oauthUserRes = null;
        try {
            HttpGet httpGet = new HttpGet(userInfoUri); // GET 요청 생성
            httpGet.addHeader("Authorization", "Bearer " + oauthToken); // 헤더에 토큰 설정

            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) { // 성공적으로 응답을 받은 경우
                String responseBody = EntityUtils.toString(response.getEntity());
                // 응답 본문을 OauthUserRes 클래스로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                oauthUserRes = objectMapper.readValue(responseBody,
                    OauthUserRes.class);
                log.info("getGithubUserInfo end: " + oauthUserRes);
            } else { // 에러 처리
                log.warn("getGithubUserInfo mid: 깃허브 유저 정보 요청 중 에러 발생 " + response.getStatusLine()
                    .getStatusCode());
                throw new GithubException();
            }

            return oauthUserRes;
        } catch (Exception e) {
            log.warn("getGithubUserInfo mid: 깃허브 유저 정보 요청 중 에러 발생", e);
            throw new GithubException();
        }
    }

}
