package com.ssafy.realrealfinal.authms.api.auth.service;

import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;

public interface AuthService {

    TokenRes login(String code, String github);

    void saveTokens(String providerId, String refreshJWTToken, String oauthAccessToken);

    void logout(String refreshToken);

    Integer getProviderIDFromAccessToken(String accessToken);

    String getGithubTokenFromJwtAccessToken(String accessToken);
}
