package com.ssafy.realrealfinal.rankms.api.rank.response;

import com.ssafy.realrealfinal.rankms.api.rank.dto.UrlRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserRankDto;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RankRes {

    Integer myRank;
    Integer pixelNum;
    List<UserRankDto> userRankList;
    List<UrlRankDto> urlRankList;

    @Builder
    public RankRes(Integer myRank, Integer pixelNum, List<UserRankDto> userRankList,
        List<UrlRankDto> urlRankList) {
        this.myRank = myRank;
        this.pixelNum = pixelNum;
        this.userRankList = userRankList;
        this.urlRankList = urlRankList;
    }
}
