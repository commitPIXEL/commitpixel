package com.ssafy.realrealfinal.userms.api.user.controller;

import com.ssafy.realrealfinal.userms.api.user.feignClient.AuthFeignClient;
import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
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
    @GetMapping("/refreshcredit")
    public ResponseEntity<?> refreshCredit(
        @RequestHeader(value = "accesstoken") String accessToken) {
        log.info("refreshCredit start: " + accessToken);
        CreditRes creditRes = userService.refreshCredit(accessToken);
        log.info("refreshCredit end: " + creditRes);
        return ResponseEntity.ok().body(creditRes);
    }

    /**
     * 픽셀 찍을 때마다 누적 픽셀 수 업데이트
     *
     * @param accessToken jwt 액세스 토큰
     * @return
     */
//    @GetMapping("/usedpixel")
//    public ResponseEntity<Integer> updateUsedPixel(
//        @RequestHeader(value = "accesstoken") String accessToken) {
//        log.info("updateUsedPixel start: " + accessToken);
//        Integer usedPixel = userService.updateUsedPixel(accessToken);
//        log.info("updateUsedPixel end: " + usedPixel);
//        return ResponseEntity.ok().body(usedPixel);
//    }

    /**
     * 건의사항 추가 - 일반 건의사항(0), url건의사항(1)
     *
     * @param accessToken jwt 액세스 토큰
     * @param boardReq    건의사항
     * @return 200 Ok(건의사항 추가 성공), 409 Conflict(url 중복), 예외처리 (accesstoken 만료)
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
     * solved 연동
     *
     * @param solvedAcId  사용자가 직접 입력한 아이디
     * @param accessToken 추후 header token으로 변경할 예정.
     * @return 인증 성공/실패
     */
    @PatchMapping("/solvedac/auth")
    public ResponseEntity<?> authSolvedAc(@RequestHeader(value = "accesstoken") String accessToken,
        @RequestParam String solvedAcId) {
        log.info("authSolvedAc start: " + solvedAcId + " " + accessToken);
        userService.authSolvedAc(solvedAcId, accessToken);
        log.info("authSolvedAc end: success");
        return ResponseEntity.ok().build();


    }

    @GetMapping("/feigntest")
    public ResponseEntity<?> feignTest(@RequestBody String test) {
        String result = authFeignClient.withBody(test);
        return ResponseEntity.ok(result);
    }


}
