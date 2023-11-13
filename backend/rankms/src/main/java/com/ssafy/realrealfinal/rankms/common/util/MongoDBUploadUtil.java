package com.ssafy.realrealfinal.rankms.common.util;

import com.ssafy.realrealfinal.rankms.api.rank.dto.FlourishDto;
import com.ssafy.realrealfinal.rankms.api.rank.dto.UserInfoDto;
import com.ssafy.realrealfinal.rankms.api.rank.feignclient.UserFeignClient;
import com.ssafy.realrealfinal.rankms.api.rank.mapper.RankMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class MongoDBUploadUtil {

    private final RedisUtil redisUtil;
    private final MongoDBUtil mongoDBUtil;
    private final UserFeignClient userFeignClient;
    private final String GITHUBNICKNAME = "githubNickname";


    /**
     * 정각부터 10분마다 mongodb에 저장.
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void mongoDBUpload() {

        //날짜 포맷 만들기.
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");
        String time = now.format(formatter);
        List<FlourishDto> flourishDtoList = new ArrayList<>();
        //flourish data import 할 때 모든 시간마다 index 생성해주기 위한 세팅값.
        FlourishDto startData = RankMapper.INSTANCE.toFLourishDto(0, "", "", "", time, 0);
        flourishDtoList.add(startData);
        Map<String, Integer> userRankMap = redisUtil.getRankList(GITHUBNICKNAME,
            -1);
        List<String> nicknameList = new ArrayList<>();
        List<Integer> pixelValueList = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : userRankMap.entrySet()) {
            nicknameList.add(entry.getKey());
            pixelValueList.add(entry.getValue());
        }
        List<UserInfoDto> userInfoDtoList = userFeignClient.getInfoFromNickname(
            nicknameList);

        for (int i = 0; i < nicknameList.size(); ++i) {
            FlourishDto flourishDto = RankMapper.INSTANCE.toFLourishDto(userInfoDtoList.get(i),
                time, pixelValueList.get(i));
            flourishDtoList.add(flourishDto);
        }
        mongoDBUtil.setData(flourishDtoList);
    }
}
