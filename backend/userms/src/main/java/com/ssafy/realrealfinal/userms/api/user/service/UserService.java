package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;

public interface UserService {

    CreditRes refreshCredit(String accessToken);

    void updateUsedPixel(String userId);

}
