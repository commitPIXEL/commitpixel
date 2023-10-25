package com.ssafy.realrealfinal.imagems.api.image.service;

import com.ssafy.realrealfinal.imagems.common.exception.ImageConvertException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public byte[] convertImage(MultipartFile file, Integer type) {

        log.info("convertImage start: " + type);

        int pixelSize = getPixelSize(type);
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            // 이미지를 작게 리사이징
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.SPEED, pixelSize);
            // 다시 원래의 크기로 확대
            BufferedImage pixelatedImage = Scalr.resize(resizedImage, Scalr.Method.SPEED, originalImage.getWidth());
            // BufferedImage를 byte[]로 변환
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(pixelatedImage, "PNG", byteArrayOutputStream);

            log.info("convertImage end");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            log.info("convertImage mid: 입출력 에러 발생");
            throw new ImageConvertException();
        }
    }

    private int getPixelSize(Integer type) {
        if (type == 1) {
            return 64;
        } else if (type == 2) {
            return 32;
        } else if (type == 3) {
            return 16;
        } else {
            return 100; // 기본 픽셀 크기
        }
    }
}
