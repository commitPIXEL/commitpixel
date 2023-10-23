package com.ssafy.realrealfinal.authms.api.auth.controller;

import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.api.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * @param request (쿠키에 담긴 토큰 추출해서 확인)
     * @return "success"
     */
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("logout start");
        String refreshToken = getRefreshTokenFromCookies(request);
        authService.logout(refreshToken);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0); // 쿠키 삭제
                cookie.setPath("/"); // 이 경로에 설정된 쿠키를 삭제
                response.addCookie(cookie); // 응답에 쿠키를 다시 추가
            }
        }
        log.info("logout end: success");
        return ResponseEntity.ok().build();
    }

    /**
     * request에서 쿠키에서 refreshtoken 추출
     *
     * @param request 쿠키 정보 위해.
     * @return refresh token
     */
    private String getRefreshTokenFromCookies(HttpServletRequest request) {
        String refreshToken = null;

        log.info("getRefreshTokenFromCookies start");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info("getRefreshTokenFromCookies end: " + refreshToken);
        return refreshToken;
    }

    /**
     * 외부 MS 요청이 들어올 때 token에서
     *
     * @param accessToken 엑세스 토큰
     * @return providerId
     */
    @GetMapping
    public Integer getProviderIdByAccessToken(String accessToken) {
        log.info("getProviderIdByJWT start: " + accessToken);
        Integer providerId = authService.getProviderIDFromAccessToken(accessToken);
        log.info("getProviderIdByJWT end: " + providerId);
        return providerId;
    }

}
