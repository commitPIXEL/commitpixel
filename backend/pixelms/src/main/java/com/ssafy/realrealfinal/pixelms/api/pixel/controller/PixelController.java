package com.ssafy.realrealfinal.pixelms.api.pixel.controller;

import com.ssafy.realrealfinal.pixelms.api.pixel.dto.AdditionalCreditDto;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.service.PixelService;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/pixel")
@RestController
public class PixelController {

    private final PixelService pixelService;
    private final RedisUtil redisUtil;

    /**
     * userms에서 credit update 시 feign으로 요청하는 메서드
     * 
     * @param additionalCreditRes
     * @return creditRes {전체 크레딧, 사용 가능 크레딧}
     */
    @GetMapping("/credit")
    public CreditRes consumeCreditEvent(@RequestBody AdditionalCreditDto additionalCreditRes) {
        log.info("consumeCreditEvent start");

        CreditRes creditRes = pixelService.updateAndSendCredit(additionalCreditRes);

        log.info("consumeCreditEvent end: " + creditRes);
        return creditRes;
    }

    /**
     * userms에서 solved.ac 연동 시 feign으로 요청하는 메서드
     * 
     * @param additionalCreditRes {providerId, 추가 크레딧}
     * @return creditRes {전체 크레딧, 사용 가능 크레딧}
     */
    @GetMapping("/solvedac")
    public CreditRes consumeSolvedAcEvent(@RequestBody AdditionalCreditDto additionalCreditRes) {
        log.info("consumeSolvedAcEvent start");

        CreditRes creditRes = pixelService.updateAndSendCredit(additionalCreditRes);

        log.info("consumeSolvedAcEvent end: " + creditRes);
        return creditRes;
    }

    /**
     * imageMS에서 scheduled로 요청 들어오면 현 redis 상태 이미지화해서 보내주는 것.
     *
     * @return 픽셀 레디스 데이터 -> 이미지
     */
    @GetMapping("/image")
    byte[] getImage() {
        log.info("getImage start");
        byte[] image = pixelService.redisToImage();
        log.info("getImage end: SUCCESS");
        return image;
    }

    @GetMapping("/image/64")
    ResponseEntity<String> toBase64Image() {
        log.info("toBase64Image start");
        String base64Image = pixelService.bufferedImageToBase64Image();
        log.info("toBase64Image end: SUCCESS");
        return ResponseEntity.ok().body(base64Image);
    }

    @GetMapping("/")
    ResponseEntity<?> test(){
        int SCALE = 10;
        for(int i=0;i<SCALE;i++){
            for(int j=0;j<SCALE;j++){
                // (x * SCALE + y) 인덱스
                Integer val = i * SCALE + j;
                String index = val.toString();

                // Red
                redisUtil.setData(index, "red", 255);
                // Green
                redisUtil.setData(index, "green", 0);
                // Blue
                redisUtil.setData(index, "blue", 0);
                // Url
                redisUtil.setData(index, "url", " ");
                // UserId
                redisUtil.setData(index, "name", " ");
            }
        }
        return ResponseEntity.ok().build();
    }

}
