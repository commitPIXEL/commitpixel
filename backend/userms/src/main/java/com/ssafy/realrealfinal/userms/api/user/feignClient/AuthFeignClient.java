package com.ssafy.realrealfinal.userms.api.user.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("auth")
public interface AuthFeignClient {

    @GetMapping("auth/token")
    Integer withQueryString(@RequestParam String accessToken);

    @GetMapping("auth/feigntest")
    String withBody(@RequestBody String test);

}
