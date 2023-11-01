package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.response.UserInfoRes;
import java.net.MalformedURLException;

public interface UserService {

    CreditRes refreshCredit(String accessToken);

//    Integer updateUsedPixel(String accessToken);

    void addBoard(String accessToken, BoardReq boardReq);

    void login(String oauthUserInfo);

    void authSolvedAc(String solvedAcId, String accessToken);

    UserInfoRes getUserInfo(String accessToken);

    void updateUrl(String accessToken, String url);
}
