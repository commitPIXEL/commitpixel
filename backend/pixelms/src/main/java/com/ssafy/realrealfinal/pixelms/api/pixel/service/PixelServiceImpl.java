package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.handler.WebSocketHandler;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.common.exception.pixel.Base64ConvertException;
import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import com.ssafy.realrealfinal.pixelms.common.util.IdNameUtil;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
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
    private final WebSocketHandler webSocketHandler;
    private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;
    private final KafkaTemplate<String, Map<Integer, Integer>> testTemplate;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";

    private final int SCALE = 512;

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
                System.out.println(key);
                Integer r = redisUtil.getIntegerData(key, "red");
                Integer g = redisUtil.getIntegerData(key, "green");
                Integer b = redisUtil.getIntegerData(key, "blue");

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
     * BufferedImage 를 base64로 변경
     *
     * @return base64 image
     */
    @Override
    public String bufferedImageToBase64Image() {
        log.info("bufferedImageToBase64Image start");
        String base64Image = null;
        BufferedImage image = redisToImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            base64Image = encoder.encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            log.warn("failed while encoding to base64", e);
            throw new Base64ConvertException();
        }
        log.info("bufferedImageToBase64Image end: SUCCESS");
        return base64Image;
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

    /**
     * UserMS 에서 update된 credit 값을 FrontEnd에 전달하고, redis에 저장
     *
     * @param record providerId와 추가된 credit을 포함
     */
    @KafkaListener(topics = "total-credit-topic", groupId = "pixel-group")
    public void consumeCreditEvent(ConsumerRecord<String, Map<String, Integer>> record) {
        log.info("consumeCreditEvent start: " + record);

        updateAndSendCredit(record);

        log.info("consumeCreditEvent end");
    }

    @KafkaListener(topics = "solvedac-update-topic", groupId = "pixel-group")
    public void consumeSolvedAcEvent(ConsumerRecord<String, Map<String, Integer>> record) {
        log.info("consumeSolvedAcEvent start: " + record);

        updateAndSendCredit(record);

        log.info("consumeSolvedAcEvent end");
    }

    private void updateAndSendCredit(ConsumerRecord<String, Map<String, Integer>> record) {
        Map<String, Integer> map = record.value();
        CreditRes creditRes = null;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            Integer providerId = Integer.valueOf(entry.getKey());
            Integer additionalCredit = entry.getValue();
            updateTotalCredit(providerId, additionalCredit);
            Integer totalCredit = getCredit(providerId, "total");
            Integer availableCredit = getAvailableCredit(providerId);
            creditRes = new CreditRes(totalCredit, availableCredit);
            webSocketHandler.sendCreditToClient(providerId, creditRes);
        }
    }

}
