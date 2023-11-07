package com.ssafy.realrealfinal.authms.api.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class OauthTokenRes {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    private String scope;

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_uri")
    private String errorUri;

    @Builder
    public OauthTokenRes(String accessToken, String tokenType, String scope, String error,
        String errorDescription, String errorUri) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.scope = scope;
        this.error = error;
        this.errorDescription = errorDescription;
        this.errorUri = errorUri;
    }
}
