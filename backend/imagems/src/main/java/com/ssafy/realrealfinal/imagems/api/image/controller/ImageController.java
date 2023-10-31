package com.ssafy.realrealfinal.imagems.api.image.controller;

import com.ssafy.realrealfinal.imagems.api.image.service.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/image")
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertImage(
        @RequestHeader(name = "accesstoken") String accessToken,
        @RequestParam(name = "file") MultipartFile file, @RequestParam Integer type) {
        log.info("convertImage start: " + type);
        byte[] convertedImage = imageService.convertImage(accessToken, file, type);
        log.info("convertImage end: 이미지 변환 완료");
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pixelated-image.jpg")
            .body(convertedImage);
    }

    @GetMapping("/test")
    public ResponseEntity<byte[]> test() throws Exception {
        log.info("test start");


        byte[] gifBytes = imageService.getGIF();

        log.info("test end");

        // 바이트 배열을 HTTP 응답 본문으로 설정하고 응답을 반환.
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_GIF) // 컨텐츠 타입을 GIF로 설정
            .body(gifBytes);
    }



}
