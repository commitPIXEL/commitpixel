package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.rankms.common.exception.rank.JsonifyException;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final int SCALE = 1024;

    @KafkaListener(topics = "pixel-update-topic", groupId = "rank-group")
    public void consumeRankingEvent(ConsumerRecord<String, String> record) {
        String pixelUpdateInfo = record.value();
        ObjectMapper objectMapper = new ObjectMapper();
        Integer oldGithubNickname;
        String oldUrl;
        Integer newGithubNickname;
        String newUrl;

        try {
            JsonNode jsonNode = objectMapper.readTree(pixelUpdateInfo);

        } catch (JsonProcessingException e) {
            throw new JsonifyException();
        }


    }
}
