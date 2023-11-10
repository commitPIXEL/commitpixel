package com.ssafy.realrealfinal.rankms.api.rank.mapper;

import com.ssafy.realrealfinal.rankms.api.rank.dto.FlourishDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UrlRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserInfoDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserRankDto;
import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;
import com.ssafy.realrealfinal.rankms.common.exception.rank.MapperException;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RankMapper {

    RankMapper INSTANCE = Mappers.getMapper(RankMapper.class);

    default RankRes toRankRes(Integer myRank, Integer pixelNum, List<UserRankDto> userRankDtoList,
        List<UrlRankDto> urlRankDtoList) throws MapperException {
        return RankRes.builder()
            .myRank(myRank)
            .pixelNum(pixelNum)
            .userRankList(userRankDtoList)
            .urlRankList(urlRankDtoList)
            .build();
    }

    UserRankDto touserRankDto(String githubNickname, Integer pixelNum) throws MapperException;

    UrlRankDto toUrlRankDto(String url, Integer pixelNum) throws MapperException;

    FlourishDto toFLourishDto(Integer providerId, String url, String githubNickname,
        String profileImage, String date, Integer value) throws MapperException;

    FlourishDto toFLourishDto(UserInfoDto userInfoDto, String date, Integer value)
        throws MapperException;
}
