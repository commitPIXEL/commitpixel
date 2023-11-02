package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.rankms.common.exception.rank.JsonifyException;
import com.ssafy.realrealfinal.rankms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final RedisUtil redisUtil;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int SCALE = 1024;
    private final String GITHUBNICKNAME = "githubNickname";
    private final String URL = "url";

    @KafkaListener(topics = "pixel-update-topic", groupId = "rank-group")
    public void consumeRankingEvent(ConsumerRecord<String, String> record) {
        String pixelUpdateInfo = record.value();
        ObjectMapper objectMapper = new ObjectMapper();
        String prevGithubNickname;
        String prevUrl;
        String currGithubNickname;
        String currUrl;

        try {
            // Kafka 로 받은 Pixel 정보 Json 역직렬화
            JsonNode jsonNode = objectMapper.readTree(pixelUpdateInfo);
            prevGithubNickname = jsonNode.get("prevGithubNickname").asText();
            prevUrl = jsonNode.get("prevUrl").asText();
            currGithubNickname = jsonNode.get("currGithubNickname").asText();
            currUrl = jsonNode.get("currUrl").asText();
        } catch (JsonProcessingException e) {
            throw new JsonifyException();
        }

        // 원래 픽셀이 있다면 관련값 개수 깎기
        if (prevGithubNickname != null) {
            redisUtil.decreaseScore(GITHUBNICKNAME, prevGithubNickname);
            redisUtil.decreaseScore(URL, prevUrl);
        }

        // 새로운 픽셀관련 개수 증가
        redisUtil.increaseScore(GITHUBNICKNAME, currGithubNickname);
        redisUtil.increaseScore(URL, currUrl);
    }
}
