package com.ssafy.realrealfinal.userms.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "solvedac_id", unique = true)
    private String solvedAcId;

    @Column(name = "github_nickname")
    @NotNull
    private String githubNickname;

    @Column(name = "profile_image")
    @NotNull
    private String profileImage;

    @Column(name = "url")
    private String url;

    @Column(name = "provider_id")
    private Integer providerId;

    public void setSolvedAcId(String solvedAcId) {
        this.solvedAcId = solvedAcId;
    }

    public void updateNickname(String githubNickname) {
        this.githubNickname = githubNickname;
    }

    @Builder
    public User(String solvedAcId, String githubNickname, String profileImage, String url,
        Integer providerId) {
        this.solvedAcId = solvedAcId;
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.url = url;
        this.providerId = providerId;
    }

    public void updateUrl(String url) {
        this.url = url;
    }
}
