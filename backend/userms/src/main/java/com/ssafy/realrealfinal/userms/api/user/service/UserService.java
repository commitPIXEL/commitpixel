package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.dto.UserInfoDto;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.response.RefreshedInfoRes;
import com.ssafy.realrealfinal.userms.api.user.response.UserInfoRes;
import java.util.List;

public interface UserService {

    RefreshedInfoRes refreshInfoFromClient(String accessToken);

    void addBoard(String accessToken, BoardReq boardReq);

    void login(String oauthUserInfo);

    CreditRes authSolvedAc(String solvedAcId, String accessToken);

    UserInfoRes getUserInfo(String accessToken);

    String updateUrl(String accessToken, String url);

    String getNickname(Integer providerId);

    List<UserInfoDto> getUserInfoListByNickname(List<String> nicknameList);
}
