package com.ssafy.realrealfinal.userms.api.user.controller;

import com.ssafy.realrealfinal.userms.api.user.response.GithubCreditRes;
import com.ssafy.realrealfinal.userms.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;

    /**
     *
     * @param accessToken
     * @return
     */
    @GetMapping
    public ResponseEntity<?> getGithubCredit(String accessToken) {
        GithubCreditRes githubCreditRes = userService.updateGithubCredit(accessToken);
        return new ResponseEntity<>(githubCreditRes, HttpStatus.OK);
    }

}
