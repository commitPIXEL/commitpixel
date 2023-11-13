package com.ssafy.realrealfinal.userms.api.user.feignClient;

import com.ssafy.realrealfinal.userms.api.user.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "pixel", url = "${feign.url}")
public interface PixelFeignClient {

    @PostMapping("pixel/credit")
    public CreditRes updateAndSendCredit(@RequestBody AdditionalCreditReq additionalCreditReq);

    @GetMapping("pixel/currentcredit")
    public CreditRes sendCredit(@RequestParam Integer providerId);

}
