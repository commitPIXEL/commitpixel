package com.ssafy.realrealfinal.rankms.api.rank.service;

import com.ssafy.realrealfinal.rankms.common.util.MongoDBUtil;
import com.ssafy.realrealfinal.rankms.common.util.RedisUtil;
import com.ssafy.realrealfinal.rankms.db.document.Flourish;
import com.ssafy.realrealfinal.rankms.db.repository.FlourishRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RankServiceImpl implements RankService {

    private final RedisUtil redisUtil;
    private final FlourishRepository flourishRepository;
    private final MongoDBUtil mongoDBUtil;

    @Override
    public void test() {
        List<Flourish> list = flourishRepository.findAll();
        System.out.println(list.toString());
//        mongoDBUtil.addStatisticsEntry("65483f1e0e5240f1d2397ff2", "12/8", "123");
//        mongoDBUtil.addNewField("65483f1e0e5240f1d2397ff2", "12/8", "123");
//        mongoDBUtil.addNewFieldByProviderId(79957085, "31/8", 123123);
        mongoDBUtil.addOrUpdateFieldByProviderIdAndUrl(79957085,"www.daum.net","heejo","hehe","100/4",123);

    }

    //닉네임 바뀔 때 같이 변경해주는 메서드 필요

    //

}

