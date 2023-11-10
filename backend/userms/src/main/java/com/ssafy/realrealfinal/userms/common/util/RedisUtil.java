package com.ssafy.realrealfinal.userms.common.util;


import java.util.Map;

import com.ssafy.realrealfinal.userms.common.exception.user.RedisNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public Long getTimeData(String key) throws RedisNotFoundException {
        log.info("getTimeData start: " + key);
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        if(valueOperations.get(key) == null){
            setTimeData(key, String.valueOf(System.currentTimeMillis()));
            log.info("getTimeData end: " + "0");
            return null;
        }else{
            Long data = Long.valueOf(valueOperations.get(key));
            log.info("getTimeData end: " + data);
            return data;
        }
    }

    public void setTimeData(String key, String value) {
        log.info("setTimeData start: " + key + " " + value);
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
        log.info("setTimeData end: success");
    }


    public void setData(String key, String type, Integer value) {
        log.info("setData start: " + key + " " + type + " " + value);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
        log.info("setData end: success");
    }

    public Map<String, String> getSolvedAcData(String key) {
        log.info("getSolvedAcData start: " + key);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        Map<String, String> data = hashOperations.entries(key);
        log.info("getSolvedAcData end: " + data);
        return data;
    }


}
