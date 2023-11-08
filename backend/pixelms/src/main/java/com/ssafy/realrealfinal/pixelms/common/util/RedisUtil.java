package com.ssafy.realrealfinal.pixelms.common.util;

import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${canvas.scale}")
    private int SCALE;

    // Canvas 전체의 색 정보를 가져오기 위한 Key 값 선언
    private final List<String> colorKeys = new ArrayList<>();
    // Canvas 전체의 픽셀 정보를 init 하기 위한 초기 Key, Value 값 선언
    private final Map<String, String> pixelKeys = new TreeMap<>();

    /**
     * 서버 실행시 Canvas 정보를 빠르게 가져오기 위한 초기 변수 선언
     */
    @PostConstruct
    public void init() {
        int scale2 = SCALE * SCALE;
        for (int i = 0; i < scale2; ++i) {
            String index = Integer.toString(i);
            colorKeys.add(index + ":R");
            colorKeys.add(index + ":G");
            colorKeys.add(index + ":B");
            pixelKeys.put(index + ":R", "255");
            pixelKeys.put(index + ":G", "255");
            pixelKeys.put(index + ":B", "255");
        }
    }

    /**
     * Redis String Value 를 get 하기 위한 메서드
     *
     * @param key Key
     * @return
     */
    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * Redis value 를 set 하기 위한 메서드
     *
     * @param key   Key
     * @param value String
     */
    public void setData(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    /**
     * Hashes Redis Value 를 가져오기 위한 메서드
     *
     * @param key  Key
     * @param type used or total
     * @return
     * @throws RedisNotFoundException
     */
    public Integer getData(String key, String type) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        String tempValue = hashOperations.get(key, type);
        if (tempValue == null) {
            throw new RedisNotFoundException();
        }
        Integer value = Integer.parseInt(tempValue);
        return value;
    }

    public void setData(String key, String type, Integer value) {
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
    }

    /**
     * Pixel Redis 에 Canvas 상태 초기화
     */
    public void initCanvasRedis() {
        stringRedisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiSet(pixelKeys);

                return null; // executePipelined 가 결과를 자동으로 반환하므로 여기서는 null 을 반환
            }
        });
    }

    /**
     * Pixel Redis 의 전체 Canvas 색정보 가져오기
     *
     * @return List 전체 색 정보 (RGB 반복하는 List)
     */
    public List<Object> getRGBValues() {
        return stringRedisTemplate.executePipelined(new SessionCallback<List<String>>() {
            @Override
            public List<String> execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiGet(colorKeys);

                return null; // executePipelined 가 결과를 자동으로 반환하므로 여기서는 null 을 반환
            }
        });
    }

    /**
     * 색칠할 위치의 이전 픽셀 정보 가져오기
     *
     * @param index x*SCALE + y
     * @return List: [url, id]
     */
    public List<Object> getPrevPixel(String index) {
        List<String> pixelInfo = new ArrayList<>();
        pixelInfo.add(index + ":url");
        pixelInfo.add(index + ":id");
        return stringRedisTemplate.executePipelined(new SessionCallback<List<String>>() {
            @Override
            public List<String> execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiGet(pixelInfo);

                return null; // executePipelined 가 결과를 자동으로 반환하므로 여기서는 null 을 반환
            }
        });
    }

    /**
     * 현재 위치에 픽셀정보 저장하기
     *
     * @param currPixelInfo [r, g, b, url, providerId]
     */
    public void setCurrPixel(Map<String, String> currPixelInfo) {
        stringRedisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiSet(currPixelInfo);

                return null; // executePipelined가 결과를 자동으로 반환하므로 여기서는 null을 반환
            }
        });
    }
}
