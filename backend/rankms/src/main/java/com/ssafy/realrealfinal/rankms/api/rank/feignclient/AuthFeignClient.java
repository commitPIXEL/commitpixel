package com.ssafy.realrealfinal.rankms.api.rank.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth", url = "${feign.url}")
public interface AuthFeignClient {

    @GetMapping("auth/token")
    Integer getProvideIdFromAccessToken(@RequestParam(value = "accessToken") String accessToken);
}
