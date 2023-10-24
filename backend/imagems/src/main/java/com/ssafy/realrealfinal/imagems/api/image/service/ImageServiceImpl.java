package com.ssafy.realrealfinal.imagems.api.image.service;

import com.ssafy.realrealfinal.imagems.api.image.feignInterface.ImageClient;
import com.ssafy.realrealfinal.imagems.common.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {
    ImageClient imageClient;
    private AwsS3Util awsS3Util;

    @Override
    public String convertImage(MultipartFile file) {

        // 취소를 누르거나 다운로드를 성공적으로 마쳤을 때 s3에 올라간 이미지 삭제하는 메서드도 구현
        return null;
    }

    public String uploadImage(MultipartFile image, String dirName) throws IOException {
        // 이미지를 S3에 업로드하고, S3 URL을 반환
        String imageUrl = awsS3Util.upload(image, dirName);
        return imageUrl;
    }

    public byte[] downloadAndTransformImage(String imageUrl) throws IOException {
        // S3에서 이미지 다운로드
        BufferedImage originalImage = ImageIO.read(new URL(imageUrl));

        // 이미지 변환 작업 (예: 크기 조절)
        BufferedImage transformedImage = transformImage(originalImage);

        // 변환된 이미지를 바이트 배열로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(transformedImage, "jpg", baos);
        return baos.toByteArray();
    }

    private BufferedImage transformImage(BufferedImage originalImage) {
        // 여기서 원하는 대로 이미지를 변환하시면 됩니다.

        return null;
    }
}
