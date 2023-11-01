package com.ssafy.realrealfinal.rankms.api.rank.mapper;

import com.ssafy.realrealfinal.rankms.api.rank.dto.UpdatePixelDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RankMapper {

    RankMapper INSTANCE = Mappers.getMapper(RankMapper.class);

    UpdatePixelDto toUpdatePixelDto(String oldGithubNickname, String oldUrl, String newGithubNickname, String newUrl);
}
