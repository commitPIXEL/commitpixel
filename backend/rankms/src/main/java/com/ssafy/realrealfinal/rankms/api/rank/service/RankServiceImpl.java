package com.ssafy.realrealfinal.rankms.api.rank.service;

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

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final RedisUtil redisUtil;
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

