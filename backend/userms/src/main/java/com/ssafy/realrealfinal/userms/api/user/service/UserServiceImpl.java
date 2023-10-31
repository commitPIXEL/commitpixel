package com.ssafy.realrealfinal.userms.api.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.userms.api.user.handler.WebSocketHandler;
import com.ssafy.realrealfinal.userms.api.user.mapper.UserMapper;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
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
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, Map<Integer, Integer>> kafkaTemplate;
    private final String TOTAL_CREDIT_KEY = "total";
    private final String USED_PIXEL_KEY = "used";

    /**
     * 커밋 수와 문제 수 불러오기
     * 15분 간격
     * pixelms에서 kafka로 호출해야 함 
     *
     * @param accessToken jwt 토큰
     * @return CreditRes
     */
    @Override
    public Integer refreshCreditFromClient(String accessToken) {
        log.info("refreshCreditFromClient start: " + accessToken);

        Integer providerId = 1; // TODO: jwt accessToken으로 providerId 얻기
        Integer refreshedCredit = refreshCredit(providerId);

        log.info("refreshCreditFromClient end: " + refreshedCredit);
        return refreshedCredit;
    }

    /**
     * 서버 내에서 호출
     *
     * @param providerId
     * @return
     */
    public Integer refreshCreditFromServer(Integer providerId) {
        log.info("refreshCreditFromServer start: " + providerId);

        Integer refreshedCredit = refreshCredit(providerId);

        log.info("refreshCreditFromServer end: " + refreshedCredit);
        return refreshedCredit;
    }

    /**
     * refreshCreditFromClient와 refreshCreditFromServer의 공통 로직
     *
     * @param providerId
     * @return
     */
    private Integer refreshCredit(Integer providerId) {
        Integer lastUpdateStatus = lastUpdateCheckUtil.getLastUpdateStatus(providerId);
        // 마지막 업데이트 시간이 15분 미만이면 변동 없음(= 0)
        if (lastUpdateStatus == -1) {
            return 0;
        }
        String userName = "유저 테이블에서 providerId로 확인한 userName"; // TODO: userRepository 사용
        String githubAccessToken = "authms로 jwt 토큰을 보내서 github 토큰을 가져옴"; // TODO: authms와 연결
        Long lastUpdateTime = lastUpdateCheckUtil.getLastUpdateTime(providerId);
        Integer commitNum = githubUtil.getCommit(githubAccessToken, userName, lastUpdateStatus, lastUpdateTime);
        // solved.ac 문제 가져오기(연동을 안 했다면 0 리턴)
        Integer solvedNum = solvedAcNewSolvedProblem(providerId);
        Integer refreshedCredit = commitNum + solvedNum;
        // pixelms와 연결된 kafka에 정보 보냄
        Map<Integer, Integer> map = Map.of(providerId, refreshedCredit);
        kafkaTemplate.send("total-credit-topic", map);
        log.info("refreshCredit end: " + refreshedCredit);
        return refreshedCredit;
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
        refreshCreditFromServer(providerId);
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
//        int total = redisUtil.getData(String.valueOf(providerId), "total") + solvedCount; // 없는 redis입니다!!!
//        redisUtil.setData(String.valueOf(providerId), "total", total); // 없는 redis입니다!!!
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