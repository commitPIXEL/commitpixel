package com.ssafy.realrealfinal.rankms.common.util;


import com.ssafy.realrealfinal.rankms.api.rank.dto.UpdatePixelDto;
import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;
import com.ssafy.realrealfinal.rankms.common.exception.rank.RedisNotFoundException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public void increaseScore(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.incrementScore(key, member, 1);
    }

    public void decreaseScore(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.incrementScore(key, member, -1);
    }

    public Integer getRank(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        Long rank = zSetOperations.reverseRank(key, member);
        if (rank == null) { // 잘못된 member 입력
            throw new RedisNotFoundException();
        } else {
            return rank.intValue() + 1; // 0부터 시작하므로 1더해서 반환
        }
    }

    public Integer getRankList(String key, Integer range) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.reverseRangeWithScores(key, 0L, range.longValue());
    }

//    public Integer getData(String key, String type) {
//        log.info("getData start: " + key + " " + type);
//        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
//        log.warn("getData mid: ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
//        log.warn("getData mid: " + hashOperations.toString());
//        Integer data = Integer.parseInt(hashOperations.get(key, type));
//        log.info("getData end: " + data);
//        return data;
//    }

    public Integer getData(String key, String type) throws Exception {
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
    }}
