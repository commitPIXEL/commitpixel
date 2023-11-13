package com.ssafy.realrealfinal.userms.api.user.controller;

import com.ssafy.realrealfinal.userms.api.user.dto.UserInfoDto;
import com.ssafy.realrealfinal.userms.api.user.feignClient.AuthFeignClient;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.request.UrlReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.response.RefreshedInfoRes;
import com.ssafy.realrealfinal.userms.api.user.response.UserInfoRes;
import com.ssafy.realrealfinal.userms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    private final String SUCCESS = "success";
    private final AuthFeignClient authFeignClient;

    /**
     * 크레딧 업데이트
     *
     * @param accessToken jwt 액세스 토큰
     * @return CreditRes, HttpStatus.OK
     */
    @GetMapping("/refreshinfo")
    public ResponseEntity<?> refreshCredit(
        @RequestHeader(value = "accesstoken") String accessToken) {
        log.info("refreshinfo start: " + accessToken);
        RefreshedInfoRes refreshedInfoRes = userService.refreshInfoFromClient(accessToken);
        log.info("refreshinfo end: " + refreshedInfoRes);
        return ResponseEntity.ok().body(refreshedInfoRes);
    }

    /**
     * 건의사항 추가 - 일반 건의사항(0), url건의사항(1)
     *
     * @param accessToken jwt 액세스 토큰
     * @param boardReq    건의사항
     * @return 200 Ok(건의사항 추가 성공), 409 Conflict(요청 url whitelist에 이미 있음)
     */
    @PostMapping("/board")
    public ResponseEntity<?> addBoard(@RequestHeader(value = "accesstoken") String accessToken,
        @RequestBody BoardReq boardReq) {
        log.info("addBoard start: " + accessToken);
        userService.addBoard(accessToken, boardReq);
        log.info("addBoard end: " + SUCCESS);
        return ResponseEntity.ok().build();
    }

    /**
     * 사용자 url 변경
     *
     * @param accessToken jwt 액세스 토큰
     * @param urlReq      사용자 업데이트 희망 url
     * @return newUrl, HttpStatus.OK
     */
    @PatchMapping("/url")
    public ResponseEntity<?> updateUrl(@RequestHeader(value = "accesstoken") String accessToken,
        @RequestBody UrlReq urlReq) {
        String url = urlReq.getUrl();
        log.info("updateUrl start: " + url);
        String newUrl = userService.updateUrl(accessToken, url);
        log.info("updateUrl end: " + newUrl);
        return ResponseEntity.ok().body(newUrl);
    }


    /**
     * solved 연동
     *
     * @param solvedAcId  사용자가 직접 입력한 아이디
     * @param accessToken 추후 header token으로 변경할 예정.
     * @return solved ac 연동되서 업데이트된 pixel 수.
     */
    @PatchMapping("/solvedac/auth")
    public ResponseEntity<?> authSolvedAc(@RequestHeader(value = "accesstoken") String accessToken,
        @RequestParam String solvedAcId) {
        log.info("authSolvedAc start: " + solvedAcId + " " + accessToken);
        CreditRes creditRes = userService.authSolvedAc(solvedAcId, accessToken);
        log.info("authSolvedAc end: " + creditRes);
        return ResponseEntity.ok(creditRes);

    }

    /**
     * feign test 용 메서드
     *
     * @param test 문자열
     * @return 문자열 추가한거.
     */
    @PostMapping("/feigntest")
    public ResponseEntity<?> feignTest(@RequestBody String test) {
        log.info("feignTest start: " + test);
        String result = authFeignClient.withBody(test);
        log.info("feignTest end: " + result);
        return ResponseEntity.ok(result);
    }

    /**
     * AccessToken으로 유저정보(닉네임, 프로필이미지, url) 가져오기
     *
     * @param accessToken jwt 액세스 토큰
     * @return 200 Ok(유저정보)
     */
    @GetMapping("/")
    public ResponseEntity<?> getUserInfo(@RequestHeader(value = "accesstoken") String accessToken) {
        log.info("getUserInfo start: ");
        UserInfoRes userInfoRes = userService.getUserInfo(accessToken);
        log.info("getUserInfo end: " + userInfoRes);
        return ResponseEntity.ok(userInfoRes);
    }

    /**
     * rank에서 feign으로 닉네임 요청
     *
     * @param providerId 깃허브 providerID
     * @return 닉네임
     */
    @GetMapping("/nickname")
    public String getNickname(@RequestParam Integer providerId) {
        log.info("getNickname start: " + providerId);
        String nickname = userService.getNickname(providerId);
        log.info("getNickname end: " + providerId);
        return nickname;
    }

    /**
     * flourish mongodb 저장용 데이터 요청
     *
     * @param nicknameList 정보 요청할 유저 닉네임 리스트
     * @return 유저 정보 리스트
     */
    @PostMapping("/info-by-nickname")
    public List<UserInfoDto> getUserInfoByNickname(@RequestBody List<String> nicknameList) {
        log.info("getUserInfoByNickname start " + nicknameList);
        List<UserInfoDto> userInfoDtoList = userService.getUserInfoListByNickname(nicknameList);
        log.info("getUserInfoByNickname end: SUCCESS");
        return userInfoDtoList;
    }
}
