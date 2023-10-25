package com.ssafy.realrealfinal.imagems.api.image.controller;

import com.ssafy.realrealfinal.imagems.api.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/image")
@RestController
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertImage(@RequestHeader(name = "accesstoken") String accessToken, @RequestParam(name = "file") MultipartFile file, @RequestParam Integer type) {
        log.info("convertImage start: " + type);
        byte[] convertedImage = imageService.convertImage(accessToken, file, type);
        log.info("convertImage end: 이미지 변환 완료");
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pixelated-image.jpg")
                .body(convertedImage);
    }

}
