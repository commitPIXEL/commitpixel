package com.ssafy.realrealfinal.rankms.db.document;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@ToString
@Setter
@Document(collection = "flourish")
public class Flourish {
    @Id
    private ObjectId _id; // org.bson.types.ObjectId type
    @Field("provider_id")
    private Integer providerId;

    @Field("Github Nickname")
    private String githubNickname;
    @Field("URL")
    private String url;
    @Field("GithubImage")
    private String githubImage;
    private Map<String, String> statistics;
    @Builder
    public Flourish(ObjectId _id, String githubNickname, String url, String githubImage,
        Map<String, String> statistics) {
        this._id = _id;
        this.githubNickname = githubNickname;
        this.url = url;
        this.githubImage = githubImage;
        this.statistics = statistics;
    }
}
