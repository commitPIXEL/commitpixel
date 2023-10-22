package com.ssafy.realrealfinal.userms.api.user.controller;

import com.ssafy.realrealfinal.userms.api.user.response.CreditRes;
import com.ssafy.realrealfinal.userms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    /**
     * 크레딧 업데이트
     * @param accessToken 깃허브 액세스 토큰
     * @return CreditRes, HttpStatus.OK
     */
    @GetMapping("/refreshcredit")
    public ResponseEntity<?> refrestCredit(@RequestHeader(value = "accesstoken") String accessToken) {
        CreditRes creditRes = userService.refreshCredit(accessToken);
        return new ResponseEntity<>(creditRes, HttpStatus.OK);
    }

}
