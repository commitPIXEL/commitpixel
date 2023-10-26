package com.ssafy.realrealfinal.imagems.api.image.feignInterface;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="authms")
public interface ImageClient {
    @GetMapping("/authms/auth/token")
    Integer getProviderIdByAccessToken(String accessToken);
}
