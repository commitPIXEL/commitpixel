package com.ssafy.realrealfinal.userms.api.user.feignClient;

import com.ssafy.realrealfinal.userms.api.user.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pixel")
public interface PixelFeignClient {

    @PostMapping("pixel/credit")
    public CreditRes updateAndSendCredit(@RequestBody AdditionalCreditReq additionalCreditReq);

}
