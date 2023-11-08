package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UrlRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.feignclient.AuthFeignClient;
import com.ssafy.realrealfinal.rankms.api.rank.feignclient.UserFeignClient;
import com.ssafy.realrealfinal.rankms.api.rank.mapper.RankMapper;
import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;
import com.ssafy.realrealfinal.rankms.common.exception.rank.JsonifyException;
import com.ssafy.realrealfinal.rankms.common.util.RedisUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.rankms.api.rank.dto.FlourishDto;
import com.ssafy.realrealfinal.rankms.api.rank.mapper.RankMapper;
import com.ssafy.realrealfinal.rankms.common.util.MongoDBUtil;
import com.ssafy.realrealfinal.rankms.common.util.RedisUtil;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {
    private final AuthFeignClient authFeignClient;
    private final UserFeignClient userFeignClient;
    private final RedisUtil redisUtil;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String GITHUBNICKNAME = "githubNickname";
    private final String URL = "url";

    private final MongoDBUtil mongoDBUtil;


    @Override
    public String getOrderedDataAsJson() {
        List<Map<String, Object>> orderedJsonList = mongoDBUtil.getOrderedData();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(orderedJsonList);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    //닉네임 바뀔 때 같이 변경해주는 메서드 필요

    //

}

