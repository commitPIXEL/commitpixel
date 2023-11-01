package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
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
     * imageMS에서 scheduled로 요청 들어오면 현 redis 상태 이미지화해서 보내주는 것.
     *
     * @return  레디스 데이터 -> 이미지
     */
    @Override
    public BufferedImage redisToImage() {




        public static BufferedImage matToBufferedImage(Mat mat) {
            int type = BufferedImage.TYPE_BYTE_GRAY;
            if (mat.channels() > 1) {
                type = BufferedImage.TYPE_3BYTE_BGR;
            }

            int bufferSize = mat.channels() * mat.cols() * mat.rows();
            byte[] bytes = new byte[bufferSize];
            mat.get(0, 0, bytes);
            BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
            final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            System.arraycopy(bytes, 0, targetPixels, 0, bytes.length);

            return image;
        }

        public static BufferedImage redisToBufferedImage() {
            // Redis 서버에 연결
            Jedis jedis = new Jedis("localhost");

            // Redis에서 픽셀 정보 가져오기
            String pixelData = jedis.get("pixel_data_key");

            // 픽셀 정보를 Mat 객체로 변환
            String[] pixelRows = pixelData.split(";");
            int height = pixelRows.length;ll
            String[] pixelRow = pixelRows[0].split(",");
            int width = pixelRow.length;
            Mat imageMat = new Mat(height, width, CvType.CV_8UC3);

            for (int y = 0; y < height; y++) {
                pixelRow = pixelRows[y].split(",");
                for (int x = 0; x < width; x++) {
                    int b = Integer.parseInt(pixelRow[x]);
                    int g = Integer.parseInt(pixelRow[x]);
                    int r = Integer.parseInt(pixelRow[x]);
                    imageMat.put(y, x, r, g, b);
                }
            }

            // Mat 객체를 BufferedImage로 변환
            BufferedImage image = matToBufferedImage(imageMat);

            // Redis 연결 종료
            jedis.close();

            return image;
        }








        return null;
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

    /**
     * KafkaListener 애노테이션을 이용해 메시지를 소비하는 메서드입니다.
     * "total-credit-topic" 토픽에서 메시지를 소비하며,
     * 그룹 ID는 "pixel-group"입니다.
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
