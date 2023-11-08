package com.ssafy.realrealfinal.rankms.api.rank.controller;
import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;
import com.ssafy.realrealfinal.rankms.api.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.realrealfinal.rankms.api.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/rank")
@RestController
public class RankController {

    private final RankService rankService;

    /**
     * 5초마다 갱신되는 랭킹 정보 보내는 메서드
     *
     * @param accessToken accessToken
     * @return 내 랭킹(없으면 null), 내 픽셀(없으면 null), 상위 10개의 픽셀 정보, 상위 10개의 url 정보
     */
    @GetMapping()
    public ResponseEntity<RankRes> getRank(
        @RequestHeader(value = "accesstoken", required = false) String accessToken) {
        log.info("getRank start");
        RankRes rankRes = rankService.getRankFromRedis(accessToken);

        log.info("getRank end: " + rankRes);
        return ResponseEntity.ok().body(rankRes);
    }

    @GetMapping("/test")
    public String findById() {
        return "true";
    }

    @GetMapping("/flourish/qkqhqkqh")
    public String toJson() {
        return rankService.getOrderedDataAsJson();
    }

}
