package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.rankms.common.util.MongoDBUtil;
import com.ssafy.realrealfinal.rankms.common.util.RedisUtil;
import com.ssafy.realrealfinal.rankms.db.document.Flourish;
import com.ssafy.realrealfinal.rankms.db.repository.FlourishRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final RedisUtil redisUtil;
    private final FlourishRepository flourishRepository;
    private final MongoDBUtil mongoDBUtil;

    @Override
    public void test() {
        List<Flourish> list = flourishRepository.findAll();
        System.out.println(list.toString());
//        mongoDBUtil.addStatisticsEntry("65483f1e0e5240f1d2397ff2", "12/8", "123");
//        mongoDBUtil.addNewField("65483f1e0e5240f1d2397ff2", "12/8", "123");
//        mongoDBUtil.addNewFieldByProviderId(79957085, "31/8", 123123);
//        mongoDBUtil.addStatisticsEntry(79957085, "www.naver.net", "heejo", "hehe",
//            "10/4", 123);
//        mongoDBUtil.addStatisticsEntry(79957085, "www.naver.net", "heejo", "hehe",
//            "10/5", 456);
//        mongoDBUtil.addStatisticsEntry(79957085, "www.naver.net", "heejo", "hehe",
//            "10/6", 789);
////        mongoDBUtil.addStatisticsEntry(123, "https://example.com", "nickname",
//            "https://example.com/image.jpg", "04/12", 150);
//        mongoDBUtil.addStatisticsEntry(123, "https://example.com", "nickname",
//            "https://example.com/image.jpg", "04/13", 300);
//        mongoDBUtil.addStatisticsEntry(123, "https://example.com", "nickname",
//            "https://example.com/image.jpg", "04/14", 450);
        mongoDBUtil.transectionTest();
    }

    @Override
    public String getOrderedDataAsJson() {
        List<Map<String, Object>> orderedJsonList = mongoDBUtil.getOrderedData();
        ObjectMapper objectMapper = new ObjectMapper();

        // 리스트를 JSON 문자열로 변환하여 반환합니다.
        try {
            return objectMapper.writeValueAsString(orderedJsonList);
        } catch (Exception e) {
            // 예외 처리 (실제 구현에서는 더 구체적인 예외 처리가 필요합니다)
            e.printStackTrace();
            return null;
        }
    }

    //닉네임 바뀔 때 같이 변경해주는 메서드 필요

    //

}

