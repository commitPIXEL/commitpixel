package com.ssafy.realrealfinal.userms.api.user.mapper;

import com.ssafy.realrealfinal.userms.api.user.request.BoardReq;
import com.ssafy.realrealfinal.userms.db.entity.Board;
import com.ssafy.realrealfinal.userms.db.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    Board toBoard(BoardReq boardReq, User user);
}
