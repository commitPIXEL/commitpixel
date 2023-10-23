package com.ssafy.realrealfinal.userms.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final RedisTemplate RedisTemplate;

    public Integer getData(String key, String type) {
        HashOperations<String, String, Integer> hashOperations = RedisTemplate.opsForHash();
        Integer data = hashOperations.get(key, type);
        return data;
    }

    public void setData(String key, String type, Integer value) {
        HashOperations<String, String, Integer> hashOperations = RedisTemplate.opsForHash();
        hashOperations.put(key, type, value);
    }

}
