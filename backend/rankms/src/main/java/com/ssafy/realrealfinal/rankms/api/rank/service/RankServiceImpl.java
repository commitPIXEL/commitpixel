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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ssafy.realrealfinal.rankms.common.util.MongoDBUtil;

@Slf4j
@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final AuthFeignClient authFeignClient;
    private final UserFeignClient userFeignClient;
    private final RedisUtil redisUtil;
    private final String GITHUBNICKNAME = "githubNickname";
    private final String URL = "url";

    private final MongoDBUtil mongoDBUtil;


    /**
     * PixelMs에서 픽셀이 하나 찍힐때마다 Ranking의 변동을 위해 kafka로 받는 메서드
     *
     * @param record 이전 픽셀정보, 현재 픽셀정보가 담겨있는 JsonNode
     */
    @KafkaListener(topics = "pixel-update-topic", groupId = "rank-group")
    public void consumeRankingEvent(ConsumerRecord<String, String> record) {
        String pixelUpdateInfo = record.value();
        log.info("consumeRankingEvent start: " + pixelUpdateInfo);

        try {
            // Kafka 로 받은 Pixel 정보 Json 역직렬화
            JsonNode jsonNode = (new ObjectMapper()).readTree(pixelUpdateInfo);

            log.info("consumeRankingEvent mid: " + jsonNode);

            // 이전 픽셀 정보는 Null 임을 확인해야 하기 때문에 먼저 JsonNode 형태로 사용
            JsonNode prevGithubNickname = jsonNode.get("prevGithubNickname");
            JsonNode prevUrl = jsonNode.get("prevUrl");

            // 현재 픽셀 정보는 정상 작동시 Null 일 수가 없으므로 String 으로 사용
            String currGithubNickname = jsonNode.get("currGithubNickname").asText();
            String currUrl = jsonNode.get("currUrl").asText();

            log.info("consumeRankingEvent mid: " + prevGithubNickname + " " + prevUrl + " "
                + currGithubNickname + " " + currUrl);

            // 원래 픽셀이 있다면 관련값 개수 감소
            if (prevGithubNickname != null) {
                redisUtil.decreaseScore(GITHUBNICKNAME, prevGithubNickname.asText());
                redisUtil.decreaseScore(URL, prevUrl.asText());
            }
            // 새로운 픽셀관련 개수 증가
            redisUtil.increaseScore(GITHUBNICKNAME, currGithubNickname);
            redisUtil.increaseScore(URL, currUrl);

            log.info("consumeRankingEvent end");
        } catch (JsonProcessingException e) {
            log.warn("consumeRankingEvent end: JsonProcessingException");
            throw new JsonifyException();
        } catch (Exception e) {
            log.warn("consumeRankingEvent end: 예상되지 않은 Exception");
            e.printStackTrace();
        }
    }

    /**
     * Redis 에 저장된 랭킹들을 가공하여 전달하는 메서드
     *
     * @param accessToken jwt token from front
     * @return RankRes 랭킹 정보들
     */
    @Override
    public RankRes getRankFromRedis(String accessToken) {
        log.info("getRankFromRedis start");

        Integer myRank = null;
        Integer pixelNum = null;

        // 토큰으로 사용자 닉네임 가져오기
        if (accessToken != null) { // 로그인된 사용자일때
            Integer providerId = authFeignClient.getProvideIdFromAccessToken(accessToken);
            String myNickname = userFeignClient.getNicknameByProviderId(providerId);

            // 내 랭킹과 픽셀개수 가져오기
            myRank = redisUtil.getRank(GITHUBNICKNAME, myNickname);
            pixelNum = redisUtil.getMemberScore(GITHUBNICKNAME, myNickname);
        }
        // myNickname 랭킹
        Map<String, Integer> userRankMap = redisUtil.getRankList(GITHUBNICKNAME,
            20); // 일단 redis 에서 Map 으로 가져오고
        List<UserRankDto> userRankDtoList = new ArrayList<>(); // List 변환
        for (Map.Entry<String, Integer> entry : userRankMap.entrySet()) {
            if (entry.getKey().equals("Visitor") || entry.getKey().equals("null")
                || entry.getValue() < 0) {
                continue; // 오류로 인해 "Visitor"와 "null", 값이 0보다 적은 값은 랭킹에서 제외
            }

            userRankDtoList.add(
                RankMapper.INSTANCE.touserRankDto(entry.getKey(), entry.getValue()));
        }
        userRankDtoList.sort((o1, o2) -> o2.getPixelNum().compareTo(o1.getPixelNum()));

        // url 랭킹
        Map<String, Integer> urlRankMap = redisUtil.getRankList(URL, 20); // 일단 redis 에서 Map 으로 가져오고
        List<UrlRankDto> urlRankDtoList = new ArrayList<>(); // List 변환
        for (Map.Entry<String, Integer> entry : urlRankMap.entrySet()) {
            if (entry.getKey().equals("null") || entry.getValue() < 0) {
                continue; // 오류로 인해 "null", 값이 0보다 적은 값은 랭킹에서 제외
            }
            urlRankDtoList.add(RankMapper.INSTANCE.toUrlRankDto(entry.getKey(), entry.getValue()));
        }
        urlRankDtoList.sort((o1, o2) -> o2.getPixelNum().compareTo(o1.getPixelNum()));

        // response 로 변환
        RankRes rankRes = RankMapper.INSTANCE.toRankRes(myRank, pixelNum, userRankDtoList,
            urlRankDtoList);

        log.info("getRankFromRedis end: " + rankRes);
        return rankRes;
    }


    /**
     * flourish에 넣기 위해 mongodb에서 읽어서 맞는 json 형태로 리턴해주는 개발자용 api
     *
     * @return flourish 용 json string
     */
    @Override
    public String getOrderedDataAsJson() {
        log.info("getOrderedDataAsJson start");
        List<Map<String, Object>> orderedJsonList = mongoDBUtil.getOrderedData();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String jsonString = objectMapper.writeValueAsString(orderedJsonList);
            log.info("getOrderedDataAsJson end: SUCCESS");
            return jsonString;
        } catch (Exception e) {
            log.warn("getOrderedDataAsJson mid: FAILED");
            return e.getMessage();
        }
    }

    //TODO: 닉네임 바뀔 때 Redis 닉네임 변경해주는 메서드 필요


}

