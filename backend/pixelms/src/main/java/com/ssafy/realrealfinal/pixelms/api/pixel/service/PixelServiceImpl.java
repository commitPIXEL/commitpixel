package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.pixelms.api.pixel.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;
import com.ssafy.realrealfinal.pixelms.common.exception.pixel.JsonifyException;
import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class PixelServiceImpl implements PixelService {

    private final RedisUtil redisUtil;
    private final KafkaTemplate<String, String> rankKafkaTemplate;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";
    @Value("${canvas.scale}")
    private int SCALE;

    /**
     * Pixel Redis 업데이트하고 RankMS 로 보내주는 메서드
     *
     * @param providerId providerId
     * @param pixelInfo  픽셀 한개의 정보 [x, y, r, g, b, url, githubNickname]
     */
    @Transactional
    @Override
    public void updatePixelAndSendRank(Integer providerId, List pixelInfo) {
        log.info("updatePixelAndSendRank start: " + providerId + " " + pixelInfo);

        Integer x = (Integer) pixelInfo.get(0);
        Integer y = (Integer) pixelInfo.get(1);
        String r = String.valueOf(pixelInfo.get(2));
        String g = String.valueOf(pixelInfo.get(3));
        String b = String.valueOf(pixelInfo.get(4));
        String url = (String) pixelInfo.get(5);
        String githubNickname = (String) pixelInfo.get(6);
        // (x * SCALE + y) 인덱스
        String index = String.valueOf(x * SCALE + y);
        // 이전 pixel url, providerId 정보 Redis에서 검색 (없으면 null)
        List<Object> prevPixelRankInfo = (List<Object>) redisUtil.getPrevPixel(index).get(0);
        // 현재 pixel 정보 Redis에 저장
        Map<String, String> currPixelInfo = Map.of(
                index + ":R", r,
                index + ":G", g,
                index + ":B", b,
                index + ":url", url,
                index + ":id", String.valueOf(providerId));
        redisUtil.setCurrPixel(currPixelInfo);

        // ==================================
        // rankMS 로 변경된 픽셀 정보 보내기
        String prevUrl = null;
        String prevGithubNickname = null;
        if (prevPixelRankInfo.get(0) != null && prevPixelRankInfo.get(1) != null) {
            prevUrl = (String) prevPixelRankInfo.get(0);
            prevGithubNickname = redisUtil.getData(prevPixelRankInfo.get(1) + ":name");
        }

        try {
            Map<String, String> pixelUpdateInfo = new TreeMap<>();
            pixelUpdateInfo.put("prevUrl", prevUrl);
            pixelUpdateInfo.put("prevGithubNickname", prevGithubNickname);
            pixelUpdateInfo.put("currUrl", url);
            pixelUpdateInfo.put("currGithubNickname", githubNickname);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonMessage = objectMapper.writeValueAsString(pixelUpdateInfo);
            rankKafkaTemplate.send("pixel-update-topic", jsonMessage);
            log.info("updatePixelAndSendRank end");
        } catch (JsonProcessingException e) {
            throw new JsonifyException();
        }

    }

    /**
     * userms와의 feign 통신용 메서드
     *
     * @param additionalCreditReq 정보 업데이트용
     * @return CreditRes
     */
    @Transactional
    @Override
    public CreditRes updateAndSendCredit(AdditionalCreditReq additionalCreditReq) {
        log.info("updateAndSendCredit start: " + additionalCreditReq);

        Integer providerId = additionalCreditReq.getProviderId();
        Integer additionalCredit = additionalCreditReq.getAdditionalCredit();
        CreditRes creditRes = null;
        // 전체 크레딧 redis 값 변경
        updateTotalCredit(providerId, additionalCredit);
        Integer totalCredit = getCreditOrInit(providerId, "total");
        Integer availableCredit = getAvailableCredit(providerId);
        creditRes = new CreditRes(totalCredit, availableCredit);

        log.info("updateAndSendCredit start: " + creditRes);
        return creditRes;
    }

    /**
     * userms에서 15분 안에 또 호출했을 때 기존 값 보내주는 메서드
     *
     * @param providerId
     * @return
     */
    @Transactional
    @Override
    public CreditRes sendCredit(Integer providerId) {
        log.info("sendCredit start: " + providerId);

        Integer totalCredit = getCreditOrInit(providerId, "total");
        Integer availableCredit = getAvailableCredit(providerId);
        CreditRes creditRes = new CreditRes(totalCredit, availableCredit);

        log.info("sendCredit end: " + creditRes);
        return creditRes;
    }

    /**
     * 누적 사용 픽셀 수 업데이트
     *
     * @param providerId 깃허브 providerId
     */
    @Transactional
    @Override
    public void updateUsedPixel(Integer providerId) {
        log.info("updateUsedPixel start: " + providerId);

        Integer usedPixel = redisUtil.getData(String.valueOf(providerId), USED_PIXEL_KEY);
        redisUtil.setData(String.valueOf(providerId) , USED_PIXEL_KEY, usedPixel + 1);

        log.info("updateUsedPixel end");
    }

    /**
     * 전체 크레딧 업데이트
     *
     * @param providerId       providerId
     * @param additionalCredit 추가 크레딧 수
     */
    @Transactional
    public void updateTotalCredit(Integer providerId, Integer additionalCredit) {
        log.info("updateTotalCredit start: " + providerId + ", " + additionalCredit);

        Integer totalCredit = getCreditOrInit(providerId, TOTAL_CREDIT_KEY);
        redisUtil.setData(String.valueOf(providerId), TOTAL_CREDIT_KEY,
                totalCredit + additionalCredit);

        log.info("updateTotalCredit end");
    }

    /**
     * 사용자가 픽셀 찍고 나서 남은 사용 가능한 픽셀 수 반환
     *
     * @param providerId 깃허브 providerId
     * @return 가용 픽셀 수
     */
    @Transactional
    @Override
    public Integer getAvailableCredit(Integer providerId) {
        log.info("getTotalAndAvailableCredit start: " + providerId);

        Integer totalCredit = getCreditOrInit(providerId, TOTAL_CREDIT_KEY);
        Integer usedPixel = getCreditOrInit(providerId, USED_PIXEL_KEY);
        Integer availableCredit = totalCredit - usedPixel;

        log.info("getTotalAndAvailableCredit end: " + availableCredit);
        return availableCredit;
    }

    /**
     * 픽셀 클릭 시 사용자 닉네임과 url을 리턴
     *
     * @param index
     * @return
     */
    @Transactional
    @Override
    public PixelInfoRes getUrlAndName(String index) {
        log.info("getUrlAndName start: " + index);

        String url = redisUtil.getData(index + ":url");
        String githubNickname = null;
        String providerId = redisUtil.getData(index + ":id");
        if (providerId != null) {
            githubNickname = redisUtil.getData(providerId + ":name");
        }

        PixelInfoRes pixelInfoRes = new PixelInfoRes(url, githubNickname);

        log.info("getUrlAndName end: " + pixelInfoRes);
        return pixelInfoRes;
    }

    /**
     * 크레딧 반환 메서드 없다면(최초 가입) 0으로 set
     *
     * @param providerId providerId
     * @param type       전체크레딧 또는 누적 사용픽셀수를 의미. total, used
     * @return Integer 크레딧
     */
    @Transactional
    public Integer getCreditOrInit(Integer providerId, String type) {
        log.info("getCreditOrInit start: " + providerId + " " + type);

        String key = String.valueOf(providerId);
        Integer credit;
        try {
            credit = redisUtil.getData(key, type);
            log.info("getCreditOrInit end: " + credit);
            return credit;
        } catch (RedisNotFoundException e) {
            redisUtil.setData(key, type, 0);
            log.warn("getCreditOrInit end: " + 0);
            return 0;
        }
    }

    /**
     * imageMS에서 scheduled로 요청 들어오면 현 redis 상태 BufferedImage로 전환해서 보내주는 것.
     *
     * @return 레디스 데이터 -> 이미지
     */
    @Override
    public byte[] redisToImage() {
        log.info("redisToImage start");
        BufferedImage bufferedImage = new BufferedImage(SCALE, SCALE, BufferedImage.TYPE_INT_ARGB);

        List<Object> rgbValues = (List<Object>) redisUtil.getRGBValues().get(0);
        int rgbIndex = -1;

        for (int x = 0; x < SCALE; ++x) {
            for (int y = 0; y < SCALE; ++y) {
                int r = Integer.parseInt((String) rgbValues.get(++rgbIndex));
                int g = Integer.parseInt((String) rgbValues.get(++rgbIndex));
                int b = Integer.parseInt((String) rgbValues.get(++rgbIndex));

                bufferedImage.setRGB(x, y, (new Color(r, g, b)).getRGB());
            }
        }

        // BufferedImage를 byte[]로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", baos);
        } catch (IOException e) {
            log.error("Error while converting BufferedImage to byte[]", e);
            return null; // Or handle the error in another way
        }

        log.info("redisToImage end: SUCCESS");
        return baos.toByteArray();
    }

    /**
     * BufferedImage 를 base64로 변경
     *
     * @return base64 image
     */
    @Override
    public String bufferedImageToBase64Image() {
        log.info("redisToBase64Image start");

        BufferedImage bufferedImage = new BufferedImage(SCALE, SCALE, BufferedImage.TYPE_INT_ARGB);
        List<Object> rgbValues = (List<Object>) redisUtil.getRGBValues().get(0);
        int rgbIndex = -1;

        for (int x = 0; x < SCALE; ++x) {
            for (int y = 0; y < SCALE; ++y) {
                int r = Integer.parseInt(rgbValues.get(++rgbIndex).toString());
                int g = Integer.parseInt(rgbValues.get(++rgbIndex).toString());
                int b = Integer.parseInt(rgbValues.get(++rgbIndex).toString());

                Color color = new Color(r, g, b);
                bufferedImage.setRGB(x, y, color.getRGB());
            }
        }

        // BufferedImage를 Base64 String으로 변환
        String base64Image = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", bos);
            byte[] imageBytes = bos.toByteArray();

            Base64.Encoder encoder = Base64.getEncoder();
            base64Image = encoder.encodeToString(imageBytes);

            bos.close();
        } catch (IOException e) {
            log.warn("Failed while encoding to base64", e);
        }

        log.info("redisToBase64Image end: SUCCESS");
        return base64Image;
    }

    /**
     * 캔버스 초기화
     */
    @Override
    public void initCanvas() {
        log.info("initCanvas start");
        redisUtil.initCanvasRedis();
        log.info("initCanvas end");
    }
}
