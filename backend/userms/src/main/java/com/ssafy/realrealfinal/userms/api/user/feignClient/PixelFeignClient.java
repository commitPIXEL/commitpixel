package com.ssafy.realrealfinal.userms.api.user.feignClient;

import com.ssafy.realrealfinal.userms.api.user.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pixel")
public interface PixelFeignClient {
    @GetMapping("/credit")
    public CreditRes updateAndSendCredit(@RequestBody AdditionalCreditReq additionalCreditReq);

}
