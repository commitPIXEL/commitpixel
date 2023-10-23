package com.ssafy.realrealfinal.userms.common.util;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LastUpdateCheckUtil {

    private final ConcurrentNavigableMap<String, Long> lastUpdateMap = new ConcurrentSkipListMap();
    private final long FIFTEEN_MINUTE = 15 * 60 * 1000;

    /**
     * 마지막 업데이트 시간 저장
     * @param userId
     */
    public void updateTime(String userId) {
        lastUpdateMap.put(userId, System.currentTimeMillis());
    }

    /**
     * 마지막 업데이트로부터 15분 이후인지 확인
     * @param userId
     * @return
     */
    public Boolean isPossibleToUpdate(String userId) {
        // 없는 사용자라면 map에 추가 후 true 리턴
        if (!lastUpdateMap.containsKey(userId)) {
            updateTime(userId);
            return true;
        }
        long lastUpdateTime = lastUpdateMap.get(userId);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUpdateTime) > FIFTEEN_MINUTE;
    }

}
