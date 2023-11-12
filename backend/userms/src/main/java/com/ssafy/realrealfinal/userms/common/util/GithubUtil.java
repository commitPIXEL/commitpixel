package com.ssafy.realrealfinal.userms.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.ssafy.realrealfinal.userms.common.exception.user.GithubException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GithubUtil {

    @Value("${github.user-info-uri}")
    private String userInfoUri;

    private final WebClient githubWebClient = WebClient
        .builder()
        .baseUrl("https://api.github.com/users")
        .defaultHeader("Accept", "application/vnd.github.v3+json")
        .build();

    // TODO: 깃허브 userInfo 얻어오고 닉네임 리턴하는 코드, 리턴 타입을 Integer가 아니라 닉네임도 포함한 새로운 dto로!

    /**
     * 깃허브 엑세스 토큰으로 깃허브의 유저 정보 요청하기 api 호출하기
     *
     * @param githubAccessToken 깃허브 엑세스 토큰
     * @return 깃허브의 닉네임
     */
    public String getGithubUserNickName(String githubAccessToken) {
        log.info("getGithubUserInfo start: " + githubAccessToken);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String githubNickname = "";
        try {
            HttpGet httpGet = new HttpGet(userInfoUri); // GET 요청 생성
            httpGet.addHeader("Authorization", "Bearer " + githubAccessToken); // 헤더에 토큰 설정

            CloseableHttpResponse response = httpClient.execute(httpGet);

            if (response.getStatusLine().getStatusCode() == 200) { // 성공적으로 응답을 받은 경우
                String responseBody = EntityUtils.toString(response.getEntity());

                // 문자열을 JSONObject로 변환
                JSONObject json = new JSONObject(responseBody);

                // JSONObject에서 "login" 값 추출
                githubNickname = json.getString("login");
                log.info("getGithubUserInfo end: " + githubNickname);
            } else { // 에러 처리
                log.warn("getGithubUserInfo mid: 깃허브 유저 정보 요청 중 에러 발생 " + response.getStatusLine()
                    .getStatusCode());
                throw new GithubException();
            }

            return githubNickname;
        } catch (Exception e) {
            log.warn("getGithubUserInfo mid: 깃허브 유저 정보 요청 중 에러 발생", e);
            throw new GithubException();
        }
    }


    /**
     * @param githubAccessToken 깃허브 토큰
     * @param githubNickname    깃허브 닉네임
     * @param lastUpdateStatus  마지막 업데이트 상태
     * @param lastUpdateTime    마지막 업데이트 일시
     * @return 커밋 수
     */
    public Integer getCommit(String githubAccessToken, String githubNickname,
                             Integer lastUpdateStatus,
                             Long lastUpdateTime) {

        log.info(
            "getCommit start: " + githubNickname + " " + lastUpdateStatus + " " + lastUpdateTime);

        // webclient로 github api 호출
        Mono<JsonNode> githubEventList = githubWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/{githubNickname}/events")
                .queryParam("per_page", 100)
                .build(githubNickname))
            .header("Authorization", "Bearer " + githubAccessToken)
            .retrieve()
            .bodyToMono(JsonNode.class);

        Integer commits = filterEvent(githubEventList, lastUpdateStatus, lastUpdateTime);

        log.info("getCommit end: " + commits);
        return commits;
    }

    /**
     * @param githubEventList
     * @param lastUpdateStatus
     * @param lastUpdateTime
     * @return
     */
    private Integer filterEvent(Mono<JsonNode> githubEventList, Integer lastUpdateStatus,
                                Long lastUpdateTime) {
        // 현재 시간 설정
        ZonedDateTime now = ZonedDateTime.now();

        // lastUpdateTime 설정
        ZonedDateTime lastUpdateZonedDateTime = Instant.ofEpochMilli(lastUpdateTime)
            .atZone(ZoneId.systemDefault());

        // type = PushEvent, 날짜 필터링 조건 설정
        return githubEventList
            .flatMapMany(jsonNode -> Flux.fromIterable(jsonNode))
            .filter(event -> "PushEvent".equals(event.path("type").asText()))
            .filter(event -> {
                ZonedDateTime eventTime = ZonedDateTime.parse(
                    event.path("created_at").asText(),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                if (lastUpdateStatus == 0) { // 최초 사용자라면 7일 이내 이벤트만
                    return eventTime.isAfter(lastUpdateZonedDateTime.minusDays(7))
                        && eventTime.isBefore(lastUpdateZonedDateTime);
                } else { // 기존 사용자이면서 마지막 업데이트로부터 15분 이후라면
                    return eventTime.isAfter(lastUpdateZonedDateTime) && eventTime.isBefore(now);
                }
            })
            .count()
            // 최근 90일 이내 아무런 이벤트가 없다면 0 리턴
            .defaultIfEmpty(0L)
            .map(count -> {
                int countAsInt = count.intValue(); // Long을 Integer로 변환
                if (lastUpdateStatus == 0) { // 최초 사용자라면 500을 추가
                    return countAsInt + 500;
                } else {
                    return countAsInt;
                }
            })
            .block();
    }
}
