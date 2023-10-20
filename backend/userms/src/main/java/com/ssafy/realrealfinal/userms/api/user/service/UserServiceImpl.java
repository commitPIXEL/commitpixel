package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.util.GithubUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final GithubUtil githubUtil;

    /**
     *
     * @param accessToken
     * @return
     */
    @Override
    public CreditRes refreshCredit(String accessToken, String githubNickname) {
        // TODO: Github REST API 호출 <- util에? ㅇㅇ
        // TODO: Solved.ac에서 문제수 가져오는 로직 구현
        // TODO: redis에서 사용자의 전체 커밋 수와 누적 사용 픽셀 수 조회 후
        // TODO: 전체 커밋 수에 API 리턴 값 더하기
        // TODO: 전체 커밋 수 - 누적 사용 픽셀 수 = 사용할 수 있는 픽셀 수
        // TODO: 누적 사용 픽셀 수, 사용할 수 있는 픽셀 수 -> GithubCreditRes 리턴
        return null;
    }
}
