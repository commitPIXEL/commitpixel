package com.ssafy.realrealfinal.userms.common.util;

import com.ssafy.realrealfinal.userms.common.exception.user.RedisNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LastUpdateCheckUtil {

    private final long FIFTEEN_MINUTE = 15 * 60 * 1000;
    private final RedisUtil redisUtil;

    /**
     * 마지막 업데이트 시간 반환
     * 없는 사용자라면 redis에 추가
     *
     * @param providerId
     * @return
     */
    public Long getLastUpdateTime(Integer providerId) {
        Long lastUpdateTime = null;
        try {
            lastUpdateTime = redisUtil.getTimeData(String.valueOf(providerId));
        } catch (RedisNotFoundException e) {
            // 로그 기록 또는 필요한 처리를 할 수 있습니다.
            log.info("getLastUpdateTime mid: 최초 가입자로 redis에 값을 저장합니다" + e);
            updateTime(providerId);
            lastUpdateTime = redisUtil.getTimeData(String.valueOf(providerId));
        }
        return lastUpdateTime;
    }


    /**
     * 마지막 업데이트 시간 저장
     *
     * @param providerId
     */
    public void updateTime(Integer providerId) {
        redisUtil.setTimeData(String.valueOf(providerId), String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 마지막 업데이트로부터 15분 이후인지 확인
     *
     * @param providerId
     * @return Integer 0 = 새로운 사용자, 1 = 15분 지남, -1 = 15분 안 지남 
     */
    public Integer getLastUpdateStatus(Integer providerId) {
        Long lastUpdateTime = getLastUpdateTime(providerId);
        if (lastUpdateTime == null) { // 최초 사용자
            return 0;
        }
        Long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) >= FIFTEEN_MINUTE) { // 15분 이후
            return 1;
        } else { // 15분 미만
            return -1;
        }
    }

}
