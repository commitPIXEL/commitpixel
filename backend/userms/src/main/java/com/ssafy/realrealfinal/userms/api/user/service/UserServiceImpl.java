package com.ssafy.realrealfinal.userms.api.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.realrealfinal.userms.api.user.feignClient.AuthFeignClient;
import com.ssafy.realrealfinal.userms.api.user.feignClient.PixelFeignClient;
import com.ssafy.realrealfinal.userms.api.user.mapper.UserMapper;
import com.ssafy.realrealfinal.userms.api.user.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.response.RefreshedInfoRes;
import com.ssafy.realrealfinal.userms.api.user.response.UserInfoRes;
import com.ssafy.realrealfinal.userms.common.exception.user.JsonifyException;
import com.ssafy.realrealfinal.userms.common.exception.user.SolvedAcAuthException;
import com.ssafy.realrealfinal.userms.common.exception.user.WhitelistNotSavedException;
import com.ssafy.realrealfinal.userms.common.util.GithubUtil;
import com.ssafy.realrealfinal.userms.common.util.LastUpdateCheckUtil;
import com.ssafy.realrealfinal.userms.common.util.RedisUtil;
import com.ssafy.realrealfinal.userms.common.util.SolvedAcUtil;
import com.ssafy.realrealfinal.userms.db.entity.Board;
import com.ssafy.realrealfinal.userms.db.entity.User;
import com.ssafy.realrealfinal.userms.db.entity.Whitelist;
import com.ssafy.realrealfinal.userms.db.repository.BoardRepository;
import com.ssafy.realrealfinal.userms.db.repository.UserRepository;
import com.ssafy.realrealfinal.userms.db.repository.WhitelistRepository;
import java.util.ArrayList;
import java.util.List;
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
    private final WhitelistRepository whitelistRepository;
    private final SolvedAcUtil solvedAcUtil;
    private final AuthFeignClient authFeignClient;
    private final KafkaTemplate<String, String> rankKafkaTemplate;
    private final KafkaTemplate<String, Map<Integer, Integer>> pixelKafkaTemplate;
    private final PixelFeignClient pixelFeignClient;

    /**
     * 커밋 수와 문제 수 불러오기 15분 간격 pixelms에서 kafka로 호출해야 함
     *
     * @param accessToken jwt 토큰
     * @return CreditRes
     */
    @Override
    public RefreshedInfoRes refreshInfoFromClient(String accessToken) {
        log.info("refreshCreditFromClient start: " + accessToken);

        Integer providerId = authFeignClient.withQueryString(accessToken);
        RefreshedInfoRes refreshedInfo = refreshInfo(providerId);

        log.info("refreshCreditFromClient end: " + refreshedInfo);
        return refreshedInfo;
    }

    /**
     * refreshCreditFromClient와 refreshCreditFromServer의 공통 로직
     *
     * @param providerId 깃허브 provider id
     * @return RefreshInfoDto
     */
    @Transactional
    public RefreshedInfoRes refreshInfo(
        Integer providerId) {
        log.info("refreshInfo start: " + providerId);

        Integer lastUpdateStatus = lastUpdateCheckUtil.getLastUpdateStatus(providerId);
        // 마지막 업데이트 시간이 15분 미만이면 변동 없음(= 0)
        if (lastUpdateStatus == -1) {
            return null;
        }
        String githubAccessToken = authFeignClient.getGithubAccessTokenByProviderId(
            String.valueOf(providerId));
        Long lastUpdateTime = lastUpdateCheckUtil.getLastUpdateTime(providerId);
        String githubNickname = githubUtil.getGithubUserNickName(
            githubAccessToken);
        User user = userRepository.findByProviderId(providerId);
        if (!user.getGithubNickname().equals(githubNickname)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String jsonMessage = objectMapper.writeValueAsString(
                    Map.of("previous", user.getGithubNickname(), "now", githubNickname));
                rankKafkaTemplate.send("rank-nickname-topic", jsonMessage);
                log.info("refreshInfo mid: kafka json data: " + jsonMessage);
            } catch (JsonProcessingException e) {
                throw new JsonifyException();
            }
            user.updateNickname(githubNickname);

        }
        // github 커밋 수 가져오기(마지막 업데이트 시점으로부터 지금까지의 변동 사항만)
        Integer commitNum = githubUtil.getCommit(githubAccessToken, githubNickname,
            lastUpdateStatus, lastUpdateTime);
        // solved.ac 문제 가져오기(연동을 안 했다면 0 리턴)
        Integer solvedNum = solvedAcNewSolvedProblem(providerId);
        Integer additionalCredit = commitNum + solvedNum;

        // pixelms와 feign으로 통신 후 프론트로 {totalCredit, availablePixel, githubNickname} 보내기
        AdditionalCreditReq additionalCreditReq = UserMapper.INSTANCE.toAdditionalCreditReq(
            providerId, additionalCredit);
        CreditRes creditRes = pixelFeignClient.updateAndSendCredit(additionalCreditReq);
        RefreshedInfoRes refreshedInfoRes = UserMapper.INSTANCE.toRefreshedInfoRes(creditRes,
            githubNickname);
        // 마지막 크레딧 업데이트 시간 갱신
        lastUpdateCheckUtil.updateTime(providerId);
        log.info("refreshInfo end: " + refreshedInfoRes);
        return refreshedInfoRes;
    }

    /**
     * 건의사항 추가. whitelist 추가 요청이면 중복 여부 체크 후, 없으면 저장
     *
     * @param accessToken 작성자 확인
     * @param boardReq    작성 내용
     */
    @Transactional
    public void addBoard(String accessToken, BoardReq boardReq) {
        Integer providerId = authFeignClient.withQueryString(accessToken);
        if (boardReq.getType() == 1) {
            String url = boardReq.getContent();
            List<Whitelist> whitelistList = new ArrayList<>();
            whitelistList = whitelistRepository.findAll();

            for (Whitelist whitelist : whitelistList) {
                if (url.contains(whitelist.getUrl())) {
                    log.info("updateUrl mid: " + whitelist.getUrl());
                    throw new WhitelistNotSavedException();
                }
            }
        }

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
            url = jsonNode.get("html_url").asText();

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

    /**
     * solvedAc 연동 본인인증 확인 절차
     *
     * @param solvedAcId  solved 아이디
     * @param accessToken 깃허브 providerid. 추후 token으로 바뀔 예정
     */
    @Override
    @Transactional
    public void authSolvedAc(String solvedAcId, String accessToken) {
        log.info("authSolvedAc start: " + solvedAcId + " " + accessToken);
        Integer providerId = authFeignClient.withQueryString(accessToken);
        Integer solvedCount = solvedAcUtil.authorizeSolvedAc(solvedAcId);
        User isUsed = userRepository.findBySolvedAcId(solvedAcId);
        if (isUsed != null) {
            throw new SolvedAcAuthException();
        }
        User user = userRepository.findByProviderId(providerId);
        user.setSolvedAcId(solvedAcId);

        String key = "solvedProblem" + providerId;
        redisUtil.setData(key, solvedAcId, solvedCount);
        //TODO:받는 쪽에서 (pixelMS) 받은 값을 total에 더해줘야.
        Map<Integer, Integer> map = Map.of(providerId, solvedCount);
        pixelKafkaTemplate.send("solvedac-update-topic", map);
        log.info("authSolvedAc mid: kafka sent data: " + solvedCount);
        log.info("authSolvedAc end: success");
    }

    @Override
    public UserInfoRes getUserInfo(String accessToken) {
        log.info("getUserInfo start: " + accessToken);
        Integer providerId = authFeignClient.withQueryString(accessToken);
        User user = userRepository.findByProviderId(providerId);
        Boolean isSolvedACAuth = user.getSolvedAcId() != null;
        UserInfoRes userInfoRes = UserMapper.INSTANCE.toUserInfoRes(user, isSolvedACAuth);
        log.info("getUserInfo end: " + userInfoRes);
        return userInfoRes;
    }

    /**
     * 사용자가 요청한 url이 Whitelist를 포함 하는지 확인
     *
     * @param accessToken jwt 토큰
     * @param url         사용자 요청 url
     * @return 1)인가 url이면 요청 url, 2)비인가 url이면 이전 사용자 url
     */
    @Transactional
    public String updateUrl(String accessToken, String url) {
        log.info("updateUrl start: " + url);
        Integer providerId = authFeignClient.withQueryString(accessToken);
        User user = userRepository.findByProviderId(providerId);
        List<Whitelist> whitelistList = new ArrayList<>();
        whitelistList = whitelistRepository.findAll();

        for (Whitelist whitelist : whitelistList) {
            if (url.contains(whitelist.getUrl())) {
                log.info("updateUrl mid: " + whitelist.getUrl());

                user.updateUrl(url);
                log.info("updateUrl end: " + user.getUrl());
                return url;
            }
        }

        String preUrl = user.getUrl();
        log.info("updateUrl end: " + preUrl);
        return preUrl;
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
        if (solvedAcId == null) {
            log.info("solvedAcNewSolvedProblem end: " + 0);
            return 0;
        }
        solvedProblem = solvedAcUtil.getSolvedCount(solvedAcId) - solvedProblem;
        redisUtil.setData(key, solvedAcId, solvedProblem);
        log.info("solvedAcNewSolvedProblem end: " + solvedProblem);
        return solvedProblem;
    }
}