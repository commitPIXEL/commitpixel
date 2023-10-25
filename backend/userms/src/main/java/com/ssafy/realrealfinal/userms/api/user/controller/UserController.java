package com.ssafy.realrealfinal.userms.api.user.controller;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board")
@RestController
public class UserController {

    private final UserService userService;

    /**
     * 크레딧 업데이트
     * @param accessToken jwt 액세스 토큰
     * @return CreditRes, HttpStatus.OK
     */
    @PostMapping("/refreshcredit")
    public ResponseEntity<?> refreshCredit(@RequestHeader(value = "accesstoken") String accessToken) {
        log.info("refreshCredit start: " + accessToken);
        CreditRes creditRes = userService.refreshCredit(accessToken);
        log.info("refreshCredit end: " + creditRes);
        return ResponseEntity.ok().body(creditRes);
    }

    /**
     * 픽셀 찍을 때마다 누적 픽셀 수 업데이트
     * @param accessToken
     * @return
     */
    @GetMapping("/usedpixel")
    public ResponseEntity<Integer> updateUsedPixel(@RequestHeader(value = "accesstoken") String accessToken) {
        log.info("updateUsedPixel start: " + accessToken);
        Integer usedPixel = userService.updateUsedPixel(accessToken);
        log.info("updateUsedPixel end: " + usedPixel);
        return ResponseEntity.ok().body(usedPixel);
    }

    /**
     * 건의사항 추가 - 일반 건의사항(0), url건의사항(1)
     *
     * @param accessToken jwt 액세스 토큰
     * @param boardReq 건의사항
     * @return 200 Ok(건의사항 추가 성공), 409 Conflict(url 중복), 예외처리 (accesstoken 만료)
     */
    @PostMapping("/board")
    public ResponseEntity<?> addBoard(@RequestHeader(value = "accesstoken") String accessToken, @RequestBody BoardReq boardReq) {
        log.info("addBoard start: " + accessToken);
        boolean isAdded = userService.addBoard(accessToken, boardReq);
        log.info("addBoard end: " + isAdded);
        return ResponseEntity.ok().body(isAdded);
    }

}
