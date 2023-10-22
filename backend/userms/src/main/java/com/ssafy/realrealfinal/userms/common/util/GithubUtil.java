package com.ssafy.realrealfinal.userms.common.util;

import com.fasterxml.jackson.databind.JsonNode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GithubUtil {

    private final WebClient githubWebClient = WebClient
        .builder()
        .baseUrl("https://api.github.com")
        .defaultHeader("Accept", "application/vnd.github.v3+json")
        .build();

    /**
     * @param accessToken
     * @param userName
     * @return
     */
    public Integer getCommit(String accessToken, String userName) {

        // webclient로 github api 호출
        Mono<JsonNode> githubEventList = githubWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/users/{username}/events")
                .queryParam("per_page", 100)
                .build(userName))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(JsonNode.class);

        // type = pushEvent, created_at < 7 days 필터링
        return githubEventList
            .flatMapMany(jsonNode -> Flux.fromIterable(jsonNode))
            .filter(event -> "PushEvent".equals(event.path("type").asText()))
            .filter(event -> {
                ZonedDateTime eventTime = ZonedDateTime.parse(
                    event.path("created_at").asText(),
                    DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                ZonedDateTime now = ZonedDateTime.now();
                return Duration.between(eventTime, now).toDays() <= 7;
            })
            .count()
            // 최근 90일 이내 아무런 이벤트가 없다면 0 리턴
            .defaultIfEmpty(0L)
            .map(Long::intValue)
            .block();
    }

}
