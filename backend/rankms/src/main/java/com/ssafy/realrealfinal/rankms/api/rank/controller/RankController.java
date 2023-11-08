package com.ssafy.realrealfinal.rankms.api.rank.controller;

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

    @GetMapping("/test")
    public String findById() {
        return "true";
    }

    @GetMapping("/flourish/qkqhqkqh")
    public String toJson() {
        return rankService.getOrderedDataAsJson();
    }

}
