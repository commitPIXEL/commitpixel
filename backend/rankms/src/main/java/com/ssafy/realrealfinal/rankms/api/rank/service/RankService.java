package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;

public interface RankService {

    RankRes getRankFromRedis(String accessToken);
}
