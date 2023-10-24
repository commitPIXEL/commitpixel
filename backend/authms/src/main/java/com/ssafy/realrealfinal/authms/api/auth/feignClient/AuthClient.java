package com.ssafy.realrealfinal.authms.api.auth.feignClient;


import com.ssafy.realrealfinal.authms.api.auth.dto.OauthUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name="userms")
public interface AuthClient {
    @PostMapping("/userms/user/login")
    void login(OauthUser oauthUser);
}
