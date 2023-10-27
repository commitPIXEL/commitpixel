package com.ssafy.realrealfinal.userms.api.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.userms.api.user.mapper.UserMapper;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.common.exception.user.JsonifyException;
import com.ssafy.realrealfinal.userms.common.exception.user.SolvedAcAuthException;
import com.ssafy.realrealfinal.userms.common.util.GithubUtil;
import com.ssafy.realrealfinal.userms.common.util.LastUpdateCheckUtil;
import com.ssafy.realrealfinal.userms.common.util.RedisUtil;
import com.ssafy.realrealfinal.userms.common.util.SolvedAcUtil;
import com.ssafy.realrealfinal.userms.db.entity.Board;
import com.ssafy.realrealfinal.userms.db.entity.User;
import com.ssafy.realrealfinal.userms.db.repository.BoardRepository;
import com.ssafy.realrealfinal.userms.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final GithubUtil githubUtil;
    private final LastUpdateCheckUtil lastUpdateCheckUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final SolvedAcUtil solvedAcUtil;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";

    /**
     * 커밋 수와 문제 수 불러오기
     *
     * @param accessToken jwt 토큰
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
     *
     * @param accessToken jwt 토큰
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
     *
     * @param userId           providerId
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
     *
     * @param userId providerId
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
     * 크레딧(전체, 누적) 반환 메서드 없다면(최초 가입) 0으로 set
     *
     * @param userId providerId
     * @param type   전체크레딧 또는 누적 사용픽셀수를 의미. total, used
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
     *
     * @param accessToken 작성자 확인
     * @param boardReq    작성 내용
     */
    public void addBoard(String accessToken, BoardReq boardReq) {
        Integer providerId = 1;
        //"유저 테이블에서 토큰으로 확인한 providerId"; // TODO: userRepository 사용
        User user = userRepository.findByProviderId(providerId);
        Board board = UserMapper.INSTANCE.toBoard(boardReq, user);
        boardRepository.save(board);
    }

    /**
     * KafkaListener 애노테이션을 이용해 메시지를 소비하는 메서드입니다. "login-topic" 토픽에서 메시지를 소비하며, 그룹 ID는
     * "user-group"입니다.
     *
     * @param record 소비된 Kafka 메시지. 메시지의 key와 value를 포함하고 있습니다.
     */
    @KafkaListener(topics = "login-topic", groupId = "user-group")
    public void consumeLoginEvent(ConsumerRecord<String, String> record) {
        String message = record.value();
        login(message);
    }

    /**
     * KafkaListener로 받은 사용자 정보로 로그인/회원가입
     *
     * @param oauthUserInfo OAuth를 통해 받은 JSON 문자열
     */
    @Override
    @Transactional
    public void login(String oauthUserInfo) {
        log.info("login start: " + oauthUserInfo);
        ObjectMapper objectMapper = new ObjectMapper();
        String githubNickname;
        String profileImage;
        Integer providerId;
        String url;
        try {
            JsonNode jsonNode = objectMapper.readTree(oauthUserInfo);

            githubNickname = jsonNode.get("login").asText();
            providerId = jsonNode.get("id").asInt();
            profileImage = jsonNode.get("avatar_url").asText();
            url = jsonNode.get("url").asText();

        } catch (JsonProcessingException e) {
            throw new JsonifyException();
        }

        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            user = UserMapper.INSTANCE.toNewUser(githubNickname, profileImage, providerId, url);
            userRepository.save(user);
        } else {
            user = UserMapper.INSTANCE.toUser(githubNickname, profileImage, user);
        }
        log.info("login end: " + user);
    }

    @Override
    @Transactional
    public void authSolvedAc(String solvedAcId, Integer providerId) {
        log.info("authSolvedAc start: " + solvedAcId + " " + providerId);
        solvedAcUtil.authId(solvedAcId);
        User isUsed = userRepository.findBySolvedAcId(solvedAcId);
        if (isUsed!=null) {
            throw new SolvedAcAuthException();
        }
        User user = userRepository.findByProviderId(providerId);
        user.setSolvedAcId(solvedAcId);
        System.out.println(user);
        log.info("authSolvedAc end: success");
    }
}