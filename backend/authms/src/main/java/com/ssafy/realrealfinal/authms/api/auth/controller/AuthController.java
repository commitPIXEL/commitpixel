package com.ssafy.realrealfinal.authms.api.auth.controller;

import com.ssafy.realrealfinal.authms.api.auth.service.AuthService;
import com.ssafy.realrealfinal.authms.common.model.CommonResponse;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login/github")
    public ResponseEntity<?> githubLogin(@RequestParam String code, HttpServletResponse response) {
        log.info("githubLogin start. code: " + code);
        String refreshToken = authService.login(code, "github");
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
