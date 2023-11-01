package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PixelServiceImpl implements PixelService {

    private final RedisUtil redisUtil;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";
    private final int SCALE = 1024;

    /**
     * 누적 사용 픽셀 수 업데이트
     *
     * @param providerId 깃허브 providerId
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
     * @param providerId 깃허브 providerId
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
     * imageMS에서 scheduled로 요청 들어오면 현 redis 상태 BufferedImage로 전환해서 보내주는 것.
     *
     * @return 레디스 데이터 -> 이미지
     */
    @Override
    public BufferedImage redisToImage() {
        log.info("redisToImage start");
        BufferedImage bufferedImage = new BufferedImage(SCALE, SCALE, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < SCALE; x++) {
            for (int y = 0; y < SCALE; y++) {
                String key = String.valueOf(x * SCALE + y);

                Integer r = redisUtil.getIntegerData(key, "R");
                Integer g = redisUtil.getIntegerData(key, "G");
                Integer b = redisUtil.getIntegerData(key, "B");

                // r, g, b 값이 null이 아니라면 이미지에 색을 적용
                if (r != null && g != null && b != null) {
                    Color color = new Color(r, g, b);
                    bufferedImage.setRGB(x, y, color.getRGB());
                }
            }
        }
        log.info("redisToImage end: SUCCESS");
        return bufferedImage;
    }

    /**
     * 크레딧 반환 메서드 없다면(최초 가입) 0으로 set
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

    /**
     * KafkaListener 애노테이션을 이용해 메시지를 소비하는 메서드입니다. "total-credit-topic" 토픽에서 메시지를 소비하며, 그룹 ID는
     * "pixel-group"입니다.
     *
     * @param record 소비된 Kafka 메시지. 메시지의 key와 value를 포함하고 있습니다.
     */
    @KafkaListener(topics = "total-credit-topic", groupId = "pixel-group")
    public void consumeCreditEvent(ConsumerRecord<String, Map<Integer, Integer>> record) {
        Map<Integer, Integer> map = record.value();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer providerId = entry.getKey();
            Integer additionalCredit = entry.getValue();
            updateTotalCredit(providerId, additionalCredit);
        }
    }

}
