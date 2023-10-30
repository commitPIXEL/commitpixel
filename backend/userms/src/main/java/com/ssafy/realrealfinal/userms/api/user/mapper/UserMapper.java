package com.ssafy.realrealfinal.userms.api.user.mapper;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.api.user.response.UserInfoRes;
import com.ssafy.realrealfinal.userms.db.entity.Board;
import com.ssafy.realrealfinal.userms.db.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Board toBoard(BoardReq boardReq, User user);

    User toNewUser(String githubNickname, String profileImage, Integer providerId, String url);

    @Mapping(source = "githubNickname", target = "githubNickname")
    @Mapping(source = "profileImage", target = "profileImage")
    User toUser(String githubNickname, String profileImage, User user);

    UserInfoRes toUserInfoRes(User user, Integer totalPixel, Integer availablePixel);
}
