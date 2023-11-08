package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.ssafy.realrealfinal.rankms.api.rank.response.RankRes;

public interface RankService {

    String getOrderedDataAsJson();

    RankRes getRankFromRedis(String accessToken);
}
