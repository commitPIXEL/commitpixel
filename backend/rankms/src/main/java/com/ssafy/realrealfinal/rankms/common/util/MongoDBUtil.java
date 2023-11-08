package com.ssafy.realrealfinal.rankms.common.util;

import com.mongodb.bulk.BulkWriteResult;
import com.ssafy.realrealfinal.rankms.api.rank.dto.FlourishDto;
import com.ssafy.realrealfinal.rankms.db.document.Flourish;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoDBUtil {

    private final MongoTemplate mongoTemplate;

    public List<Map<String, Object>> getOrderedData() {
        // MongoDB에서 전체 Flourish 객체를 가져옵니다.
        List<Flourish> flourishes = mongoTemplate.findAll(Flourish.class);

        List<Map<String, Object>> orderedJsonList = new ArrayList<>();

        // 날짜와 시간을 포함하는 형식으로 SimpleDateFormat 객체를 초기화합니다.
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm");

        for (Flourish flourish : flourishes) {
            Map<String, Object> orderedMap = new LinkedHashMap<>();

            // 기본 필드를 추가합니다.
            orderedMap.put("URL", flourish.getUrl());
            orderedMap.put("Github Nickname", flourish.getGithubNickname());
            orderedMap.put("GithubImage", flourish.getGithubImage());

            // 날짜와 시간에 따라 통계 데이터를 TreeMap을 사용하여 정렬합니다.
            TreeMap<String, String> sortedStatistics = new TreeMap<>(
                Comparator.comparing(date -> {
                    try {
                        // 문자열을 날짜로 파싱합니다.
                        return dateFormat.parse(date);
                    } catch (ParseException e) {
                        // 파싱에 실패한 경우 예외를 던집니다.
                        throw new IllegalArgumentException("Invalid date format for sorting", e);
                    }
                })
            );
            sortedStatistics.putAll(flourish.getStatistics());
            orderedMap.putAll(sortedStatistics);

            orderedJsonList.add(orderedMap);
        }

        return orderedJsonList;
    }

    public void setData(List<FlourishDto> entries) {
        BulkOperations bulkOps = mongoTemplate.bulkOps(BulkOperations.BulkMode.ORDERED,
            Flourish.class);

        for (FlourishDto entry : entries) {
            Criteria criteria = new Criteria().andOperator(
                Criteria.where("provider_id").is(entry.getProviderId()),
                Criteria.where("URL").is(entry.getUrl())
            );
            Query query = new Query(criteria);
            Update update = new Update();
            update.set("statistics." + entry.getDate(), entry.getValue());

            update.setOnInsert("provider_id", entry.getProviderId());
            update.setOnInsert("URL", entry.getUrl());
            update.setOnInsert("Github Nickname", entry.getGithubNickname());
            update.setOnInsert("GithubImage", entry.getGithubImage());

            bulkOps.upsert(query, update);
        }

        BulkWriteResult result = bulkOps.execute(); // Execute the bulk operation
    }


}
