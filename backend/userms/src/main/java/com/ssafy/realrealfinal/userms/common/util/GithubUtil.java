package com.ssafy.realrealfinal.userms.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GithubUtil {

    @Value("${github.url}")
    private String GITHUB_URL;

    private final WebClient githubWebClient = WebClient
        .builder()
        .baseUrl(GITHUB_URL)
        .defaultHeader("Accept", "application/vnd.github.v3+json")
        .build();

    // TODO: 깃허브 userInfo 얻어오고 닉네임 리턴하는 코드, 리턴 타입을 Integer가 아니라 닉네임도 포함한 새로운 dto로!

    /**
     * @param githubAccessToken 깃허브 토큰
     * @param githubNickname          깃허브 닉네임
     * @param lastUpdateStatus  마지막 업데이트 상태
     * @param lastUpdateTime    마지막 업데이트 일시
     * @return 커밋 수
     */
    public Integer getCommit(String githubAccessToken, String githubNickname, Integer lastUpdateStatus,
        Long lastUpdateTime) {

        log.info("getCommit start: " + githubNickname + " " + lastUpdateStatus + " " + lastUpdateTime);

        // webclient로 github api 호출
        Mono<JsonNode> githubEventList = githubWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/users/{githubNickname}/events")
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
            .map(Long::intValue)
            .block();
    }


}
