package com.ssafy.realrealfinal.rankms.db.document;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@ToString
@Document(collection = "${spring.data.mongodb.collection}")
public class Flourish {

    @Id
    private ObjectId _id;
    @Field("provider_id")
    private Integer providerId;
    @Field("Github Nickname")
    private String githubNickname;
    @Field("URL")
    private String url;
    @Field("GithubImage")
    private String profileImage;
    private Map<String, String> statistics;

    @Builder
    public Flourish(ObjectId _id, Integer providerId, String githubNickname, String url,
        String profileImage, Map<String, String> statistics) {
        this._id = _id;
        this.providerId = providerId;
        this.githubNickname = githubNickname;
        this.url = url;
        this.profileImage = profileImage;
        this.statistics = statistics;
    }
}
