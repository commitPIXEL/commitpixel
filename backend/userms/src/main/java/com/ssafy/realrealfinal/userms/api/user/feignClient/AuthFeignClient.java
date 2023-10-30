package com.ssafy.realrealfinal.userms.api.user.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="auth",url = "http://k9a709.p.ssafy.io:8181")
public interface AuthFeignClient {

    @GetMapping("auth/token")
    Integer withQueryString(@RequestParam String accessToken);

    @PostMapping("auth/feigntest")
    String withBody(@RequestBody String test);

}
