package com.ssafy.realrealfinal.authms.api.auth.controller;

import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.api.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
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

    /**
     * 로그인 처리
     *
     * @param code     인가코드
     * @param response refreshtoken, acesstoken 담긴 dto 반환
     * @return HttpStatus.OKs
     */
    @GetMapping("/login/github")
    public ResponseEntity<?> githubLogin(@RequestParam String code, HttpServletResponse response) {
        log.info("githubLogin start. code: " + code);
        TokenRes tokenRes = authService.login(code, "github");
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshtoken",
                tokenRes.getJwtRefreshToken())
            .path("/") // too-t.com 하위 url 모두 저장 유지
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
        response.addHeader("accesstoken", tokenRes.getJwtAccessToken());
        System.out.println("githubLogin end: " + tokenRes);
        return ResponseEntity.ok().build();
    }


}
