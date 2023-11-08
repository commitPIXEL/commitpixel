package com.ssafy.realrealfinal.userms.common.util;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LastUpdateCheckUtil {

    private final ConcurrentNavigableMap<Integer, Long> lastUpdateMap = new ConcurrentSkipListMap();
    private final long FIFTEEN_MINUTE = 15 * 60 * 1000;

    /**
     * 마지막 업데이트 시간 반환
     *
     * @param providerId
     * @return
     */
    public Long getLastUpdateTime(Integer providerId) {
        return lastUpdateMap.get(providerId);
    }

    /**
     * 마지막 업데이트 시간 저장
     *
     * @param providerId
     */
    public void updateTime(Integer providerId) {
        lastUpdateMap.put(providerId, System.currentTimeMillis());
    }

    /**
     * 마지막 업데이트로부터 15분 이후인지 확인
     * 없는 사용자라면 map에 추가
     *
     * @param providerId
     * @return Integer 0 = 새로운 사용자, 1 = 15분 지남, -1 = 15분 안 지남 
     */
    public Integer getLastUpdateStatus(Integer providerId) {
        if (!lastUpdateMap.containsKey(providerId)) { // 없는 사용자
            updateTime(providerId);
            return 0;
        }
        long lastUpdateTime = lastUpdateMap.get(providerId);
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastUpdateTime) >= FIFTEEN_MINUTE) { // 15분 이후
            return 1;
        } else { // 15분 미만
            return -1;
        }
    }

}
