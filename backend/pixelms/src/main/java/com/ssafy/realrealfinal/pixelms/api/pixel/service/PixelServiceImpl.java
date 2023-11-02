package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.handler.WebSocketHandler;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;
import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import com.ssafy.realrealfinal.pixelms.common.util.IdNameUtil;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PixelServiceImpl implements PixelService {

    private final RedisUtil redisUtil;
    private final IdNameUtil idNameUtil;
    private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";
    private final int SCALE = 512;

    /**
     * 누적 사용 픽셀 수 업데이트
     *
     * @param providerId
     */
    @Override
    public void updateUsedPixel(Integer providerId) {
        log.info("updateUsedPixel start: " + providerId);

        Integer usedPixel = redisUtil.getIntegerData(String.valueOf(providerId), USED_PIXEL_KEY);
        redisUtil.setData(String.valueOf(providerId), USED_PIXEL_KEY, usedPixel + 1);

        log.info("updateUsedPixel end");
    }

    /**
     * 사용자가 픽셀 찍고 나서 남은 사용 가능한 픽셀 수 반환
     *
     * @param providerId
     * @return 가용 픽셀 수
     */
    @Override
    public Integer getAvailableCredit(Integer providerId) {
        log.info("getTotalAndAvailableCredit start: " + providerId);

        Integer totalCredit = getCredit(providerId, TOTAL_CREDIT_KEY);
        Integer usedPixel = getCredit(providerId, USED_PIXEL_KEY);
        Integer availableCredit = totalCredit - usedPixel;

        log.info("getTotalAndAvailableCredit end: " + availableCredit);
        return availableCredit;
    }

    /**
     * 크레딧 반환 메서드
     * 없다면(최초 가입) 0으로 set
     *
     * @param providerId providerId
     * @param type       전체크레딧 또는 누적 사용픽셀수를 의미. total, used
     * @return Integer 크레딧
     */
    private Integer getCredit(Integer providerId, String type) {
        log.info("getCredit start: " + providerId + " " + type);
        String key = String.valueOf(providerId);
        Integer credit = 0;
        try {
            credit = redisUtil.getIntegerData(key, type);
            log.info("getCredit end: " + credit);
            return credit;
        } catch (RedisNotFoundException e) {
            redisUtil.setData(key, type, 0);
            log.warn("getCredit end: " + 0);
            return 0;
        }

    }

    /**
     * 전체 크레딧 업데이트
     *
     * @param providerId       providerId
     * @param additionalCredit 추가 크레딧 수
     */
    private void updateTotalCredit(Integer providerId, Integer additionalCredit) {
        log.info("updateTotalCredit start: " + providerId + ", " + additionalCredit);

        Integer totalCredit = getCredit(providerId, TOTAL_CREDIT_KEY);
        redisUtil.setData(String.valueOf(providerId), TOTAL_CREDIT_KEY,
                totalCredit + additionalCredit);

        log.info("updateTotalCredit end");
    }

    @Override
    public void updatePixelRedisAndSendRank(Integer providerId, List pixelInfo) {
        log.info("updatePixelRedis start: " + pixelInfo);

        // (x * SCALE + y) 인덱스
        String index = String.valueOf((Integer) pixelInfo.get(0) * SCALE + (Integer) pixelInfo.get(1));

        // 이전 유저 닉네임과 url 정보(없으면 null)
        String prevUrl = redisUtil.getStringData(index, "url");
        String prevName = idNameUtil.getNameById(Integer.valueOf(redisUtil.getStringData(index, "providerId")));

        // Red
        redisUtil.setData(index, "R", (Integer) pixelInfo.get(2));
        // Green
        redisUtil.setData(index, "G", (Integer) pixelInfo.get(3));
        // Blue
        redisUtil.setData(index, "B", (Integer) pixelInfo.get(4));
        // Url
        redisUtil.setData(index, "url", (String) pixelInfo.get(5));
        // providerId
        redisUtil.setData(index, "id", providerId);

        // rank로 이전, 현재 정보 보내기
        Map<String, String> map = Map.of(
                "prevUrl", prevUrl,
                "prevGithubNickname", prevName,
                "currUrl", (String) pixelInfo.get(5),
                "currGithubNickname", (String) pixelInfo.get(6));
        kafkaTemplate.send("pixel-update-topic", map);
    }

    @Override
    public CreditRes updateAndSendCredit(Object object) {
        log.info("updateAndSendCredit start: " + object);

        CreditRes creditRes = null;
//        updateTotalCredit(providerId, additionalCredit);
//        Integer totalCredit = getCredit(providerId, "total");
//        Integer availableCredit = getAvailableCredit(providerId);
//        creditRes = new CreditRes(totalCredit, availableCredit);

        log.info("updateAndSendCredit start: " + creditRes);
        return creditRes;
    }

    @Override
    public PixelInfoRes getUrlAndName(String index) {
        log.info("getUrlAndName start: " + index);

        String url = redisUtil.getStringData(index, "url");
        String githubNickname = idNameUtil.getNameById(Integer.valueOf(redisUtil.getStringData(index, "providerId")));

        PixelInfoRes pixelInfoRes = new PixelInfoRes(url, githubNickname);

        log.info("getUrlAndName end: " + pixelInfoRes);
        return pixelInfoRes;
    }


}
