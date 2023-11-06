package com.ssafy.realrealfinal.rankms.common.util;

import com.ssafy.realrealfinal.rankms.db.document.Flourish;
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

    public void addStatisticsEntry(String id, String date, String value) {
        // ObjectId를 이용하여 업데이트할 문서를 식별하는 Query 객체를 생성합니다.
        Query query = new Query(Criteria.where("_id").is(new ObjectId(id)));

        // 업데이트할 내용을 지정하는 Update 객체를 생성합니다.
        // statistics 맵에 새로운 키-값 쌍을 추가합니다.
        Update update = new Update().set("statistics." + date, value);

        // 업데이트를 수행합니다.
        mongoTemplate.updateFirst(query, update, Flourish.class);
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
        // 추가적인 필드가 있다면 여기에 setOnInsert를 계속 추가하면 됩니다.

        mongoTemplate.upsert(query, update, Flourish.class);
    }


}
