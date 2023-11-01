package com.ssafy.realrealfinal.pixelms.common.util;

import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public Integer getIntegerData(String key, String type) throws RedisNotFoundException {
        log.info("getData start: " + key + " " + type);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        log.warn("getData mid: " + hashOperations.get(key, type));
        Integer data = Integer.parseInt(hashOperations.get(key, type));
        log.info("getData end: " + data);
        return data;

    }

    public String getStringData(String key, String type) throws RedisNotFoundException {
        log.info("getData start: " + key + " " + type);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        log.warn("getData mid: " + hashOperations.get(key, type));
        String data = hashOperations.get(key, type);
        log.info("getData end: " + data);
        return data;

    }


    public void setData(String key, String type, Integer value) {
        log.info("setData start: " + key + " " + type + " " + value);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
        log.info("setData end: success");
    }

    public void setData(String key, String type, String value) {
        log.info("setData start: " + key + " " + type + " " + value);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
        log.info("setData end: success");
    }


}
