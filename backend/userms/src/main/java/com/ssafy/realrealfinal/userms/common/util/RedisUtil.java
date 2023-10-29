package com.ssafy.realrealfinal.userms.common.util;


import java.util.Map;

import com.ssafy.realrealfinal.userms.common.exception.user.RedisNotFoundException;
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

//    public Integer getData(String key, String type) {
//        log.info("getData start: " + key + " " + type);
//        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
//        log.warn("getData mid: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.warn("getData mid: " + hashOperations.toString());
//        Integer data = Integer.parseInt(hashOperations.get(key, type));
//        log.info("getData end: " + data);
//        return data;
//    }

    public Integer getData(String key, String type) throws RedisNotFoundException {
        log.info("getData start: " + key + " " + type);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();

        log.warn("getData mid: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        log.warn("getData mid: " + hashOperations.get(key, type));
        Integer data = Integer.parseInt(hashOperations.get(key, type));
        log.info("getData end: " + data);
        return data;

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
        log.info("setData end: " + data);
        return data;
    }


}
