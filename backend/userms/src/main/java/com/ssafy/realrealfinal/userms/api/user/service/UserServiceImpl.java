package com.ssafy.realrealfinal.userms.api.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.userms.api.user.mapper.UserMapper;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.common.exception.user.JsonifyException;
import com.ssafy.realrealfinal.userms.common.exception.user.RedisNotFoundException;
import com.ssafy.realrealfinal.userms.common.exception.user.SolvedAcAuthException;
import com.ssafy.realrealfinal.userms.common.util.GithubUtil;
import com.ssafy.realrealfinal.userms.common.util.LastUpdateCheckUtil;
import com.ssafy.realrealfinal.userms.common.util.RedisUtil;
import com.ssafy.realrealfinal.userms.common.util.SolvedAcUtil;
import com.ssafy.realrealfinal.userms.db.entity.Board;
import com.ssafy.realrealfinal.userms.db.entity.User;
import com.ssafy.realrealfinal.userms.db.repository.BoardRepository;
import com.ssafy.realrealfinal.userms.db.repository.UserRepository;
import java.util.Map;
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
     * 15분 간격
     * @param accessToken jwt 토큰
     * @return CreditRes
     */
    @Override
    public CreditRes refreshCredit(String accessToken) {
        log.info("refreshCredit start: " + accessToken);

        Integer providerId = 1; // TODO: jwt accessToken으로 providerId 얻기
        Integer lastUpdateStatus = lastUpdateCheckUtil.getLastUpdateStatus(providerId);
        // 마지막 업데이트 시간이 15분 미만이면 기존 정보 불러오기
        if (lastUpdateStatus == -1) {
            return getTotalAndAvailableCredit(providerId);
        }
        String userName = "유저 테이블에서 토큰으로 확인한 userName"; // TODO: userRepository 사용
        String githubAccessToken = "authms로 jwt 토큰을 보내서 github 토큰을 가져옴"; // TODO: authms와 연결
        Long lastUpdateTime = lastUpdateCheckUtil.getLastUpdateTime(providerId);
        Integer commitNum = githubUtil.getCommit(githubAccessToken, userName, lastUpdateStatus, lastUpdateTime);
        Integer solvedNum = 0; // TODO: Solved.ac에서 문제수 가져오는 로직 구현
        // 커밋 + 문제 수 합친 값으로 전체 크레딧 업데이트
        updateTotalCredit(providerId, commitNum + solvedNum);
        CreditRes creditRes = getTotalAndAvailableCredit(providerId);

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
     * @param providerId
     * @param additionalCredit 추가 크레딧 수
     */
    private void updateTotalCredit(Integer providerId, Integer additionalCredit) {
        log.info("updateTotalCredit start: " + providerId + ", " + additionalCredit);

        Integer totalCredit = getCredit(providerId, TOTAL_CREDIT_KEY);
        redisUtil.setData(String.valueOf(providerId), TOTAL_CREDIT_KEY,
            totalCredit + additionalCredit);

        log.info("updateTotalCredit end");
    }

    /**
     * 전체 크레딧, 사용 가능 크레딧 반환
     *
     * @param providerId providerId
     * @return CreditRes
     */
    private CreditRes getTotalAndAvailableCredit(Integer providerId) {
        log.info("getTotalAndAvailableCredit start: " + providerId);
        Integer totalCredit = getCredit(providerId, TOTAL_CREDIT_KEY);
        Integer usedPixel = getCredit(providerId, USED_PIXEL_KEY);
        CreditRes creditRes = new CreditRes(totalCredit, totalCredit - usedPixel);

        log.info("getTotalAndAvailableCredit end: " + creditRes);
        return creditRes;
    }

    /**
     * 크레딧(전체, 누적) 반환 메서드
     * 없다면(최초 가입) 0으로 set
     * @param providerId providerId
     * @param type       전체크레딧 또는 누적 사용픽셀수를 의미. total, used
     * @return Integer 크레딧
     */
    private Integer getCredit(Integer providerId, String type) {
        log.info("getCredit start: " + providerId + " " + type);
        String key = String.valueOf(providerId);
        Integer credit = 0;
        try {
            credit = redisUtil.getData(key, type);
            log.info("getCredit end: " + credit);
            return credit;
        } catch (RedisNotFoundException e) {
            redisUtil.setData(key, type, 0);
            log.warn("getCredit end: " + 0);
            return 0;
        }

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
            //TODO: 깃허브 커밋수 가져와서 넣어두는 작업 (7일치)
        } else {
            user = UserMapper.INSTANCE.toUser(githubNickname, profileImage, user);
        }
        log.info("login end: " + user);
    }

    /**
     * solvedAc 연동 본인인증 확인 절차
     *
     * @param solvedAcId solved 아이디
     * @param providerId 깃허브 providerid. 추후 token으로 바뀔 예정
     */
    @Override
    @Transactional
    public void authSolvedAc(String solvedAcId, Integer providerId) {
        log.info("authSolvedAc start: " + solvedAcId + " " + providerId);
        Integer solvedCount = solvedAcUtil.authorizeSolvedAc(solvedAcId);
        User isUsed = userRepository.findBySolvedAcId(solvedAcId);
        if (isUsed != null) {
            throw new SolvedAcAuthException();
        }
        User user = userRepository.findByProviderId(providerId);
        user.setSolvedAcId(solvedAcId);

        String key = "solvedProblem" + providerId;
        redisUtil.setData(key, solvedAcId, solvedCount);
        int total = getCredit(providerId, "total") + solvedCount;
        redisUtil.setData(String.valueOf(providerId), "total", total);
        log.info("authSolvedAc end: success");
    }

    /**
     * 새로고침 때 solvedAc 새로 푼 문제수만 가져오기
     *
     * @param providerId 깃허브 provider id
     * @return 새로 푼 문제수 (차이)
     */
    public Integer solvedAcNewSolvedProblem(Integer providerId) {
        log.info("solvedAcNewSolvedProblem start: " + providerId);
        String key = "solvedProblem" + providerId;
        Map<String, String> data = redisUtil.getSolvedAcData(key);
        String solvedAcId = null;
        Integer solvedProblem = 0;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            solvedAcId = entry.getKey();
            solvedProblem = Integer.parseInt(entry.getValue());
        }
        solvedProblem = solvedAcUtil.getSolvedCount(solvedAcId) - solvedProblem;
        log.info("solvedAcNewSolvedProblem end: " + solvedProblem);
        return solvedProblem;
    }
}