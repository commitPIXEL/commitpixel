package com.ssafy.realrealfinal.rankms.common.util;


import com.ssafy.realrealfinal.rankms.common.exception.rank.RedisNotFoundException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

/**
 * Redis 의 정렬된 상태를 유지하는 Sorted-Set(Zset)을 사용했다. Key, Member(Value), Score로 이루어져 있고, Score 기준으로 정렬이
 * 된다
 * <p>
 * Key는 "githubnickname", "url"을 사용했고, member는 각 값, score는 차지한 픽셀 개수를 사용했다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 순위를 가져오는 메서드 우리는 nickname 기준으로 자신의 순위를 가져옴
     *
     * @param key    "githubNickname" 문자열
     * @param member githubNickname 값
     * @return 본인 등수
     */
    public Integer getRank(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        Long rank = zSetOperations.reverseRank(key, member);
        if (rank == null) { // 잘못된 member 입력
            throw new RedisNotFoundException();
        } else {
            return rank.intValue() + 1; // 순위가 0부터 시작하므로 1더해서 반환
        }
    }

    /**
     * score를 가져오는 메서드 우리는 nickname 기준으로 자신의 차지 픽셀개수를 가져옴
     *
     * @param key    "githubNickname"
     * @param member githubNickname 값
     * @return 본인 픽셀수
     */
    public Integer getMemberScore(String key, String member) {
        Double score = stringRedisTemplate.opsForZSet().score(key, member);
        if (score == null) {
            throw new RedisNotFoundException();
        } else {
            return score.intValue();
        }
    }

    /**
     * 상위 [range]개의 member와 score를 정렬된 상태인 Map으로 반환하는 함수
     *
     * @param key   "githubNickname", "url"
     * @param range return할 데이터 개수, 상위 N개
     * @return member, score가 담긴 정렬된 Map
     */
    public Map<String, Integer> getRankList(String key, Integer range) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        Set<TypedTuple<String>> resultSet = zSetOperations.reverseRangeWithScores(key, 0L,
            range.longValue());
        Map<String, Integer> sortedMap = new TreeMap<>(Collections.reverseOrder());
        for (ZSetOperations.TypedTuple<String> typedTuple : resultSet) {
            sortedMap.put(typedTuple.getValue(), typedTuple.getScore().intValue());
        }

        return sortedMap;
    }

    /**
     * 픽셀 개수 하나 증가
     *
     * @param key    "githubNickname", "url"
     * @param member 증가대상
     */
    public void increaseScore(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.incrementScore(key, member, 1);
    }

    /**
     * 픽셀 개수 하나 감소
     *
     * @param key    "githubNickname", "url"
     * @param member 감소대상
     */
    public void decreaseScore(String key, String member) {
        ZSetOperations<String, String> zSetOperations = stringRedisTemplate.opsForZSet();
        zSetOperations.incrementScore(key, member, -1);
    }
}
