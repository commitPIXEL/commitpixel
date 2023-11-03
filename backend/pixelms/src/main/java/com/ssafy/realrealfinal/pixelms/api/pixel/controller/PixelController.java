package com.ssafy.realrealfinal.pixelms.api.pixel.controller;

import com.ssafy.realrealfinal.pixelms.api.pixel.service.PixelService;
import com.ssafy.realrealfinal.pixelms.common.util.RedisUtil;
import java.awt.image.BufferedImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        pixelService.test();
        return ResponseEntity.ok().build();
    }
}
