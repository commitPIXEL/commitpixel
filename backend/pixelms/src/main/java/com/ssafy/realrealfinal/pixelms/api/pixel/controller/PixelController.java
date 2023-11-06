package com.ssafy.realrealfinal.pixelms.api.pixel.controller;

import com.ssafy.realrealfinal.pixelms.api.pixel.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.service.PixelService;
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

    /**
     * userms에서 credit update 시 feign으로 요청하는 메서드
     *
     * @param additionalCreditRes 추가된 credit 수
     * @return creditRes {전체 크레딧, 사용 가능 크레딧}
     */
    @GetMapping("/credit")
    public CreditRes updateAndSendCredit(@RequestBody AdditionalCreditReq additionalCreditRes) {
        log.info("consumeCreditEvent start");

        CreditRes creditRes = pixelService.updateAndSendCredit(additionalCreditRes);

        log.info("consumeCreditEvent end: " + creditRes);
        return creditRes;
    }

    /**
     * imageMS에서 scheduled로 요청 들어오면 현 redis 상태 이미지화해서 보내주는 것.
     *
     * @return 픽셀 레디스 데이터 -> 이미지
     */
    @GetMapping("/image")
    public byte[] getImage() {
        log.info("getImage start");
        byte[] image = pixelService.redisToImage();
        log.info("getImage end: SUCCESS");
        return image;
    }

    /**
     * 개발자용 현재 redis 상태를 -> bufferedImage 로 변환 후 -> base64 로 전환하는 메서드
     *
     * @return String 형태의 BufferedImage
     */
    @GetMapping("/image/64")
    public ResponseEntity<String> toBase64Image() {
        log.info("toBase64Image start");
        String base64Image = pixelService.bufferedImageToBase64Image();
        log.info("toBase64Image end: SUCCESS");
        return ResponseEntity.ok().body(base64Image);
    }
}
