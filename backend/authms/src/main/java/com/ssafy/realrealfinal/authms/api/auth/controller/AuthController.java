package com.ssafy.realrealfinal.authms.api.auth.controller;

import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.api.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        log.info("githubLogin start: code " + code);
        TokenRes tokenRes = authService.login(code, "github");
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshtoken",
                tokenRes.getJwtRefreshToken())
            .path("/") // 하위 url 모두 저장 유지
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());
        response.addHeader("accesstoken", tokenRes.getJwtAccessToken());
        System.out.println("githubLogin end: " + tokenRes);
        return ResponseEntity.ok().build();
    }

    /**
     * 로그아웃. redis, cookie에서 JWT 토큰 정보 삭제
     *
     * @param coookie  refreshtoken
     * @param response 쿠키 지울거
     * @return "success"
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(
        @CookieValue(value = "refreshtoken", required = false) Cookie coookie,
        HttpServletResponse response) {
        log.info("logout start");
        String refreshToken = coookie.getValue();
        authService.logout(refreshToken);
        coookie.setMaxAge(0); // 쿠키 삭제
        coookie.setPath("/"); // 이 경로에 설정된 쿠키를 삭제
        response.addCookie(coookie); // 응답에 쿠키를 다시 추가

        log.info("logout end: success");
        return ResponseEntity.ok().build();
    }

    /**
     * 외부 MS 요청이 들어올 때 token에서
     *
     * @param accessToken 엑세스 토큰
     * @return providerId
     */
    @GetMapping("/token")
    public Integer getProviderIdByAccessToken(@RequestParam String accessToken) {
        log.info("getProviderIdByJWT start: " + accessToken);
        Integer providerId = authService.getProviderIDFromAccessToken(accessToken);
        log.info("getProviderIdByJWT end: " + providerId);
        return providerId;
    }


    @PostMapping("/feigntest")
    String feignTest(@RequestBody String test) {
        log.info("feignTest start: " + test);
        String result = test + " connected by feign";
        log.info("feignTest end: " + result);
        return result;
    }

    @GetMapping("/token/github")
    public String getGithubAccessTokenByJwtAccessToken(@RequestParam String accessToken){
        log.info("getGithubAccessTokenByJwtAccessToken start: "+accessToken);
        String githubAccessToken = authService.getGithubTokenFromJwtAccessToken(accessToken);
        log.info("getGithubAccessTokenByJwtAccessToken end: "+accessToken);
        return githubAccessToken;
    }
}
