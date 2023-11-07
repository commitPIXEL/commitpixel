package com.ssafy.realrealfinal.authms.api.auth.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OauthUserRes {

    private String login;
    private Integer id;
    private String avatar_url;
    private String html_url;

    @Builder
    public OauthUserRes(String login, Integer id, String avatar_url, String html_url) {
        this.login = login;
        this.id = id;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
    }
}