package com.ssafy.realrealfinal.rankms.api.rank.feignclient;

import com.ssafy.realrealfinal.rankms.api.rank.dto.UserInfoDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user", url = "${feign.url}")
public interface UserFeignClient {

    @GetMapping("user/nickname")
    String getNicknameByProviderId(@RequestParam(value = "providerId") Integer providerId);

    @PostMapping("user/info-by-nickname")
    List<UserInfoDto> getInfoFromNickname(@RequestBody List<String> nicknameList);
}
