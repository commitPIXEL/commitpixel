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

    /**
     * 인증. 입력받은 아이디로 bio 조회해서 문자열 존재하는지 확인
     *
     * @param solvedAcId solved 아이디
     * @return 푼 문제수
     */
    public Integer authorizeSolvedAc(String solvedAcId) {
        log.info("authorizeSolvedAc start: " + solvedAcId);
        //다르면 예외던지기.
        String bio = null;
        Integer solvedCount = null;
        try {
            HttpClient client = HttpClients.createDefault();
            URIBuilder uriBuilder = new URIBuilder(AUTH_URL);
            uriBuilder.addParameter("handle", solvedAcId);

            // GET 요청 설정
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = client.execute(httpGet);

            // 응답에서 HTTP 엔티티 받기
            String result = EntityUtils.toString(response.getEntity());

            // 문자열을 JSONObject로 변환
            JSONObject json = new JSONObject(result);

            // JSONObject에서 "bio" 값 추출
            bio = json.getString("bio");
            solvedCount = json.getInt("solvedCount");

        } catch (Exception e) {
            log.warn("authorizeSolvedAc mid: FAIL");
            throw new APIRequestException();
        }
        if (!bio.contains(message)) {
            log.warn("authorizeSolvedAc mid: bio mismatch");
            throw new SolvedAcAuthException();
        }

        log.info("authorizeSolvedAc end: " + solvedCount);
        return solvedCount;
    }

    /**
     * 새로 푼 문제 가져오기용
     *
     * @param solvedAcId 아이디
     * @return 푼 문제수
     */
    public int getSolvedCount(String solvedAcId) {
        log.info("getSolvedCount start: " + solvedAcId);
        //다르면 예외던지기.
        Integer solvedCount = null;
        try {
            HttpClient client = HttpClients.createDefault();
            URIBuilder uriBuilder = new URIBuilder(AUTH_URL);
            uriBuilder.addParameter("handle", solvedAcId);

            // GET 요청 설정
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            HttpResponse response = client.execute(httpGet);

            // 응답에서 HTTP 엔티티 받기
            String result = EntityUtils.toString(response.getEntity());

            // 문자열을 JSONObject로 변환
            JSONObject json = new JSONObject(result);

            // JSONObject에서 "solvedCount" 값 추출
            solvedCount = json.getInt("solvedCount");

        } catch (Exception e) {
            log.warn("getSolvedCount mid: FAIL");
            throw new APIRequestException();
        }

        log.info("getSolvedCount end: " + solvedCount);
        return solvedCount;
    }


}
