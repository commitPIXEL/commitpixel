package com.ssafy.realrealfinal.userms.api.user.service;

import com.ssafy.realrealfinal.userms.api.user.response.GithubCreditRes;

public interface UserService {

    GithubCreditRes updateGithubCredit(String accessToken);

}
