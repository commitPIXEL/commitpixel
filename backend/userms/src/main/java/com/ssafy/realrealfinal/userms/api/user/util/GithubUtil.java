package com.ssafy.realrealfinal.userms.api.user.util;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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
     * @param githubNickname
     * @return
     */
    public Integer getCommit(String accessToken, String githubNickname) {

        // webclient로 github api 호출
        Mono<JsonNode> githubEventList = githubWebClient
            .get()
            .uri(uriBuilder -> uriBuilder
                .path("/users/{username}/events")
                .queryParam("per_page", 100)
                .build(githubNickname))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .bodyToMono(JsonNode.class);

        // TODO: type, created_at 필터링

        return null;
    }

}
