package com.ssafy.realrealfinal.rankms.api.rank.mapper;

import com.ssafy.realrealfinal.rankms.api.rank.dto.UpdatePixelDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UrlRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RankMapper {

    RankMapper INSTANCE = Mappers.getMapper(RankMapper.class);

    UpdatePixelDto toUpdatePixelDto(String prevGithubNickname, String prevUrl,
        String currGithubNickname, String currUrl);

    RankRes toRankRes(Integer myRank, Integer pixelNum, List<UserRankDto> userRankDtoList,
        List<UrlRankDto> urlRankDtoList);

    UserRankDto touserRankDto(String nickname, Integer score);

    UrlRankDto toUrlRankDto(String url, Integer score);
}
