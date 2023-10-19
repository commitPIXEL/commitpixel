package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.response.GithubCreditRes;

public class UserServiceImpl implements UserService {

    /**
     *
     * @param accessToken
     * @return
     */
    @Override
    public GithubCreditRes updateGithubCredit(String accessToken) {
        // TODO: Github REST API 호출 <- util에?
        // TODO: redis에서 사용자의 전체 커밋 수와 누적 사용 픽셀 수 조회 후
        // TODO: 전체 커밋 수에 API 리턴 값 더하기
        // TODO: 전체 커밋 수 - 누적 사용 픽셀 수 = 사용할 수 있는 픽셀 수
        // TODO: 누적 사용 픽셀 수, 사용할 수 있는 픽셀 수 -> GithubCreditRes 리턴
        return null;
    }
}
