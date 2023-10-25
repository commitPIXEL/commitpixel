package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.common.util.GithubUtil;
import com.ssafy.realrealfinal.userms.common.util.LastUpdateCheckUtil;
import com.ssafy.realrealfinal.userms.common.util.RedisUtil;
import com.ssafy.realrealfinal.userms.db.repository.BoardRepository;
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
    private final BoardRepository boardRepository;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";

    /**
     * 커밋 수와 문제 수 불러오기
     * @param accessToken
     * @return CreditRes
     */
    @Override
    public CreditRes refreshCredit(String accessToken) {
        log.info("refreshCredit start: " + accessToken);

        String userId = "유저 테이블에서 토큰으로 확인한 providerId"; // TODO: userRepository 사용
        if (!lastUpdateCheckUtil.isPossibleToUpdate(userId)) {
            return getTotalAndAvailableCredit(userId);
        }
        String userName = "유저 테이블에서 토큰으로 확인한 userName"; // TODO: userRepository 사용
        String githubAccessToken = "authms로 jwt 토큰을 보내서 github 토큰을 가져옴"; // TODO: authms와 연결
        Integer commitNum = githubUtil.getCommit(githubAccessToken, userName);
        Integer solvedNum = 0; // TODO: Solved.ac에서 문제수 가져오는 로직 구현

        updateTotalCredit(userId, commitNum + solvedNum);
        CreditRes creditRes = getTotalAndAvailableCredit(userId);

        log.info("refreshCredit end: " + creditRes);
        return creditRes;
    }

    /**
     * 사용자가 픽셀을 찍을 때 마다 누적 사용 픽셀 수 + 1
     * @param accessToken
     * @return Integer 누적 사용 픽셀 수
     */
    @Override
    public Integer updateUsedPixel(String accessToken) {
        log.info("updateUsedPixel start: " + accessToken);

        String userId = "유저 테이블에서 토큰으로 확인한 providerId"; // TODO: userRepository 사용
        Integer usedPixel = redisUtil.getData(userId, USED_PIXEL_KEY);
        redisUtil.setData(userId, USED_PIXEL_KEY, usedPixel + 1);
        Integer updatedUsedPixel = redisUtil.getData(userId, USED_PIXEL_KEY);

        log.info("updateUsedPixel end: " + updatedUsedPixel);
        return updatedUsedPixel;
    }

    /**
     * 전체 크레딧 업데이트
     * @param userId
     * @param additionalCredit 추가 크레딧 수
     */
    private void updateTotalCredit(String userId, Integer additionalCredit) {
        log.info("updateTotalCredit start: " + userId + ", " + additionalCredit);

        Integer totalCredit = getCredit(userId, TOTAL_CREDIT_KEY);
        redisUtil.setData(userId, TOTAL_CREDIT_KEY, totalCredit + additionalCredit);

        log.info("updateTotalCredit end");
    }

    /**
     * 전체 크레딧, 사용 가능 크레딧 반환
     * @param userId
     * @return CreditRes
     */
    private CreditRes getTotalAndAvailableCredit(String userId) {
        log.info("getTotalAndAvailableCredit start: " + userId);

        Integer totalCredit = getCredit(userId, TOTAL_CREDIT_KEY);
        Integer usedPixel = getCredit(userId, USED_PIXEL_KEY);
        CreditRes creditRes = new CreditRes(totalCredit, totalCredit - usedPixel);

        log.info("getTotalAndAvailableCredit end: " + creditRes);
        return creditRes;
    }

    /**
     * 크레딧(전체, 누적) 반환 메서드
     * 없다면(최초 가입) 0으로 set
     * @param userId
     * @param type
     * @return Integer 크레딧
     */
    private Integer getCredit(String userId, String type) {
        Integer credit = redisUtil.getData(userId, type);
        if (credit == null) {
            redisUtil.setData(userId, type, 0);
            return 0;
        }
        return credit;
    }


    /**
     * 건의사항 추가
     * @param accessToken
     * @param boardReq
     * @return true, false
     */
    public boolean addBoard(String accessToken, BoardReq boardReq) {
        log.info("addBoard start: " + accessToken + ", " + boardReq);
        String userId = "유저 테이블에서 토큰으로 확인한 providerId"; // TODO: userRepository 사용
        // Board board = "userId와 boardReq를 entity로 변환" // TODO: boardMapper 사용
        // boardRepository.save(board);
        return true;
    }
}