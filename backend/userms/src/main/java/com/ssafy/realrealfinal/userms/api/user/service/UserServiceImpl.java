package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.common.util.GithubUtil;
import com.ssafy.realrealfinal.userms.common.util.LastUpdateCheckUtil;
import com.ssafy.realrealfinal.userms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final GithubUtil githubUtil;
    private final LastUpdateCheckUtil lastUpdateCheckUtil;
    private final RedisUtil redisUtil;

    /**
     * 커밋 수와 문제 수 불러오기
     * @param accessToken
     * @return
     */
    @Override
    public CreditRes refreshCredit(String accessToken) {
        String userId = "유저 테이블에서 토큰으로 확인한 providerId";
        // treemap 속 유저의 마지막 업데이트 시간으로부터 15분이 안 지났다면 null 리턴 (혹은 그냥 지금 redis에 저장한 값 그대로?)
        if (!lastUpdateCheckUtil.isPossibleToUpdate(userId)) {
            return null;
        }
        // 유저 이름으로 Github REST API 호출
        String userName = "유저 테이블에서 토큰으로 확인한 userName";
        Integer commitNum = githubUtil.getCommit(accessToken, userName);
        // TODO: Solved.ac에서 문제수 가져오는 로직 구현
        Integer solvedNum = 0;
        updateTotalCredit(userId, commitNum + solvedNum);
        // CreditRes {누적 사용 픽셀 수, 사용할 수 있는 픽셀 수} 리턴
        return getTotalAndAvailableCredit(userId);
    }

    /**
     * 사용자가 픽셀을 찍을 때 마다 누적 사용 픽셀 수 + 1
     * @param userId
     */
    @Override
    public void updateUsedPixel(String userId) {
        // 누적 사용 픽셀 수 + 1
        Integer usedPixel = redisUtil.getData(userId, "used");
        redisUtil.setData(userId, "used", usedPixel + 1);
    }

    public CreditRes getTotalAndAvailableCredit(String userId) {
        // TODO: 사용자의 전체 크레딧과 누적 사용 픽셀 수 조회 후 사용 가능 픽셀 수 리턴
        Integer totalCredit = redisUtil.getData(userId, "total");
        Integer usedPixel = redisUtil.getData(userId, "used");
        return new CreditRes(totalCredit, totalCredit - usedPixel);
    }

    public void updateTotalCredit(String userId, Integer additionalCredit) {
        // 전체 크레딧에 API 리턴 값 더하기
        Integer totalCredit = redisUtil.getData(userId, "total");
        redisUtil.setData(userId, "total", totalCredit + additionalCredit);
    }
}
