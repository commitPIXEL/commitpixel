package com.ssafy.realrealfinal.imagems.api.image.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="auth")
public interface AuthFeignClient {
    @GetMapping("auth/token")
    Integer getProviderIdByAccessToken(String accessToken);
}
