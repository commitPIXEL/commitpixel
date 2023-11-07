package com.ssafy.realrealfinal.rankms.common.util;

import com.mongodb.MongoException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.ssafy.realrealfinal.rankms.db.document.Flourish;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoDBUtil {

    private final MongoTemplate mongoTemplate;

    public void addStatisticsEntry(Integer providerId, String url, String githubNickname, String githubImage,
        String date, Integer value) {
        Criteria criteria = new Criteria().andOperator(
            Criteria.where("provider_id").is(providerId),
            Criteria.where("URL").is(url)
        );
        Query query = new Query(criteria);

        Update update = new Update();
        // "statistics" 맵에 "MM/dd" 형식의 날짜와 값을 직접 넣습니다.
        update.set("statistics." + date, value);

        update.setOnInsert("provider_id", providerId);
        update.setOnInsert("URL", url);
        update.setOnInsert("Github Nickname", githubNickname);
        update.setOnInsert("GithubImage", githubImage);

        mongoTemplate.upsert(query, update, Flourish.class);
    }


    public void addNewField(String id, String fieldName, String value) {
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));
        Update update = new Update().set(fieldName, value);
        mongoTemplate.updateFirst(query, update, Flourish.class);
    }

    public void addNewFieldByProviderId(Integer providerId, String date, Integer value) {
        Query query = new Query(Criteria.where("provider_id").is(providerId));
        Update update = new Update().set(date, value);
        mongoTemplate.updateFirst(query, update, Flourish.class);
    }

    public void addOrUpdateFieldByProviderIdAndUrl(
        Integer providerId, String url, String githubNickname, String githubImage,
        String date, Integer value
    ) {
        Criteria criteria = new Criteria().andOperator(
            Criteria.where("provider_id").is(providerId),
            Criteria.where("URL").is(url)
        );
        Query query = new Query(criteria);
        Update update = new Update();

        // 기존 문서에 적용될 변경 사항
        update.set(date, value);

        // 새 문서 삽입시 사용될 기본값 설정
        update.setOnInsert("provider_id", providerId);
        update.setOnInsert("URL", url);
        update.setOnInsert("Github Nickname", githubNickname);
        update.setOnInsert("GithubImage", githubImage);

        mongoTemplate.upsert(query, update, Flourish.class);
    }


    public List<Map<String, Object>> getOrderedData() {
        // MongoDB에서 전체 Flourish 객체를 가져옵니다.
        List<Flourish> flourishes = mongoTemplate.findAll(Flourish.class);
        System.out.println(flourishes);
        // JSON 문자열로 변환될 리스트
        List<Map<String, Object>> orderedJsonList = new ArrayList<>();

        // 날짜 형식 정의
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

        for (Flourish flourish : flourishes) {
            Map<String, Object> orderedMap = new LinkedHashMap<>();

            // 기본 필드를 추가합니다.
            orderedMap.put("URL", flourish.getUrl());
            orderedMap.put("Github Nickname", flourish.getGithubNickname());
            orderedMap.put("GithubImage", flourish.getGithubImage());

            // 날짜 관련 통계 데이터를 TreeMap을 사용하여 정렬합니다.
            // dd/MM 형식의 Comparator를 사용하여 정렬
            TreeMap<String, String> sortedStatistics = new TreeMap<>(
                Comparator.comparing(date -> {
                    try {
                        return dateFormat.parse(date);
                    } catch (Exception e) {
                        throw new IllegalArgumentException("Invalid date format for sorting");
                    }
                })
            );
            sortedStatistics.putAll(flourish.getStatistics());
            orderedMap.putAll(sortedStatistics);

            // 정렬된 맵을 리스트에 추가합니다.
            orderedJsonList.add(orderedMap);
        }
        return orderedJsonList;
    }

    public void ttt(ClientSession session, Integer providerId, String url, String githubNickname, String githubImage, String date, Integer value) {
        Criteria criteria = new Criteria().andOperator(
            Criteria.where("provider_id").is(providerId),
            Criteria.where("URL").is(url)
        );
        Query query = new Query(criteria);

        Update update = new Update();
        update.set("statistics." + date, value);

        update.setOnInsert("provider_id", providerId);
        update.setOnInsert("URL", url);
        update.setOnInsert("Github Nickname", githubNickname);
        update.setOnInsert("GithubImage", githubImage);

        // Replace "YourCollectionName" with the actual name of your collection
        mongoTemplate.upsert(query, update, Flourish.class, "real_real_final", session);
    }
    public void transectionTest(){
        MongoClient mongoClient = null;

        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();

            ttt(session, 79957085, "www.edu.ssafy.com", "heejo", "hehe", "10/4", 123);

            // 다른 데이터베이스 작업을 추가할 수 있습니다.

            session.commitTransaction();
        } catch (MongoException me) {
//            ClientSession session.abortTransaction();
            System.out.println(me.getMessage());
        }

    }
}
