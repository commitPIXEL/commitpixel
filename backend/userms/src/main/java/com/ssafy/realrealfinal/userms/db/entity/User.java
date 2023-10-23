package com.ssafy.realrealfinal.userms.db.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "solvedac_id")
    private String solvedAcId;

    @Column(name = "github_nickname")
    private String githubNickname;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "url")
    private String url;

    @Column(name = "provider_id")
    private String providerId;
}
