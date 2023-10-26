package com.ssafy.realrealfinal.userms.db.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "\"user\"")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer id;

    @Column(name = "solvedac_id")
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
    private String providerId;

    @Builder
    public User(String solvedAcId, String githubNickname, String profileImage, String url,
        String providerId) {
        this.solvedAcId = solvedAcId;
        this.githubNickname = githubNickname;
        this.profileImage = profileImage;
        this.url = url;
        this.providerId = providerId;
    }
}
