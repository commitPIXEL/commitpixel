package com.ssafy.realrealfinal.pixelms.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Slf4j
@Component
public class IdNameUtil {

    private HashMap<Integer, String> idNameMap = new HashMap<>();

    public void updateMap(Integer providerId, String githubNickname) {
        idNameMap.put(providerId, githubNickname);
    }

    public String getNameById(Integer providerId) {
        return idNameMap.get(providerId);
    }
}
