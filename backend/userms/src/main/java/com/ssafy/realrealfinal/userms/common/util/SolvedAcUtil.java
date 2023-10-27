package com.ssafy.realrealfinal.userms.common.util;

import com.ssafy.realrealfinal.userms.common.exception.user.APIRequestException;
import com.ssafy.realrealfinal.userms.common.exception.user.SolvedAcAuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Component
public class SolvedAcUtil {

    @Value("${solvedac.auth-url}")
    private String AUTH_URL;

    @Value("${solvedac.message}")
    private String message;


    public void authId(String authId) {
        log.info("authId start: "+authId);
        //다르면 예외던지기.
        HttpClient client = HttpClients.createDefault();
        String bio = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(AUTH_URL);
            uriBuilder.addParameter("handle", authId);

            // GET 요청 설정
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = client.execute(httpGet);

            // 응답에서 HTTP 엔티티 받기
            String result = EntityUtils.toString(response.getEntity());

            // 문자열을 JSONObject로 변환
            JSONObject json = new JSONObject(result);

            // JSONObject에서 "bio" 값 추출
            bio = json.getString("bio");
            System.out.println(bio);

        } catch (Exception e) {
            log.warn("authId mid: FAIL");
            throw new APIRequestException();
        }
        if (!bio.contains(message)) {
            log.warn("authId mid: bio mismatch");
            throw new SolvedAcAuthException();
        }

        log.info("authId end: success");

    }

}
