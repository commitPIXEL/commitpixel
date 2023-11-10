package com.ssafy.realrealfinal.imagems.api.image.controller;

import com.ssafy.realrealfinal.imagems.api.image.service.ImageService;

import com.ssafy.realrealfinal.imagems.common.util.S3UploadUtil;
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
    private final S3UploadUtil s3UploadUtil;

    @PostMapping("/convert")
    public ResponseEntity<byte[]> convertImage(@RequestParam(name = "file") MultipartFile file, @RequestParam Integer type) {
        log.info("convertImage start: " + type);
        byte[] convertedImage = imageService.convertImage(file, type);
        log.info("convertImage end: 이미지 변환 완료");
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_JPEG)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pixelated-image.jpg")
            .body(convertedImage);
    }

    /**
     * 요청시 s3에 저장된 이미지 24시간 치 데이터를 gif로 만들어서 반환
     *
     * @return gif
     */
    @GetMapping("/timelapse")
    public ResponseEntity<byte[]> timelapse() {
        log.info("timelapse start");
        byte[] gifBytes = imageService.getGif();
        log.info("timelapse end: SUCCESS");

        // 바이트 배열을 HTTP 응답 본문으로 설정하고 응답을 반환.
        return ResponseEntity.ok()
            .contentType(MediaType.IMAGE_GIF) // 컨텐츠 타입을 GIF로 설정
            .body(gifBytes);
    }


    @GetMapping("/test")
    public ResponseEntity<?> test() {
        log.info("test start");
        s3UploadUtil.S3Upload();
        log.info("test end");
        return ResponseEntity.ok().build();
    }
}
