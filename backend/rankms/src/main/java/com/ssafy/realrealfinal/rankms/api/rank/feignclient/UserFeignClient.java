package com.ssafy.realrealfinal.rankms.api.rank.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "${feign.url}")
public interface UserFeignClient {

    @GetMapping("user/nickname")
    String getNicknameByProviderId(@RequestParam(value = "providerId") Integer providerId);
}
