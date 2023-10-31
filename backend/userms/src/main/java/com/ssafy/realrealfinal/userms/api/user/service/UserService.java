package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;

public interface UserService {

    Integer refreshCreditFromClient(String accessToken);

    void addBoard(String accessToken, BoardReq boardReq);

    void login(String oauthUserInfo);

    void authSolvedAc(String solvedAcId, Integer providerId);
}
