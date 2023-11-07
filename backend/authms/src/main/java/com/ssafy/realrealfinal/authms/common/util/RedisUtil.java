package com.ssafy.realrealfinal.authms.common.util;

import com.ssafy.realrealfinal.authms.common.exception.auth.RedisNotDeletedException;
import com.ssafy.realrealfinal.authms.common.exception.auth.RedisNotSavedException;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 토큰 레디스에서 가져요기. 없을 경우 null return
     *
     * @param key redis 키 ("token 분류" + userId)
     * @return 값 (토큰)
     */
    public String getData(String key) {
        log.info("getData start: " + key);
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        String token = valueOperations.get(key);
        log.info("getData end: " + token);
        return token;
    }

    /**
     * 토큰 저장할때 사용. 이건 시간 지나면 알아서 redis에서 삭제됨. 없어서 요청 들어오면 null 리턴
     *
     * @param key      redis 키 ("token 분류" + userId)
     * @param value    redis value (token)
     * @param duration redis 만료기간
     */
    public void setDataWithExpire(String key, String value, Long duration) {
        log.info("setDataWithExpire start: " + key + " " + value + " " + duration);
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
        // 저장된 값을 검증
        String storedValue = valueOperations.get(key);
        if (storedValue == null || !storedValue.equals(value)) {
            throw new RedisNotSavedException();
        }
        log.info("setDataWithExpire end: success");
    }

    /**
     * redis에서 데이터 삭제. 문제 발생시 에러
     *
     * @param key "token 분류" + userId
     */
    public void deleteData(String key) {
        log.info("deleteData start: " + key);
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            throw new RedisNotDeletedException();
        }
        log.info("deleteData end: success");
    }

}