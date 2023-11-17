package com.ssafy.realrealfinal.authms.api.auth.mapper;

import com.ssafy.realrealfinal.authms.api.auth.response.TokenRes;
import com.ssafy.realrealfinal.authms.common.exception.auth.MapperException;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthMapper {

    AuthMapper INSTANCE = Mappers.getMapper(AuthMapper.class);

    TokenRes toTokenRes(String jwtAccessToken, String jwtRefreshToken, String nickname)
        throws MapperException;

}
