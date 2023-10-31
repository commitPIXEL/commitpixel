package com.ssafy.realrealfinal.imagems.common.util;

import com.ssafy.realrealfinal.imagems.api.image.feignClient.PixelFeignClient;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class S3UploadUtil {

    private final PixelFeignClient pixelFeignClient;
    private final AwsS3Util awsS3Util;

    /**
     * 이미지를 정각과 30분마다 Amazon S3에 업로드.
     * 업로드될 이미지는 pixelFeignClient로 pixcelMS에게 현재 캔버스 상태 image로 요청.
     * 이미지는 "yyyy-MM-dd" 폴더에 저장.
     * 이미지 파일의 현재 시간 (예: 12:45 -> 12.5, 00:01 -> 0.0) 이름으로 파일 저장.
     */
//    @Scheduled(cron = "0 0,30 * * * ?")
    public void S3Upload() {
        BufferedImage image = pixelFeignClient.getImage();
        LocalDateTime now = LocalDateTime.now();
        // 저장할 폴더.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int minutes = now.getMinute();
        // 이미지 이름
        String fileName;

        if (minutes < 30) {
            // 정각일 때 .0을 붙여서 저장
            fileName = String.format("%.1f", (double) now.getHour());
        } else {
            fileName = String.format("%.1f", now.getHour() + 0.5);
        }
        String folderName = now.format(formatter);
        awsS3Util.uploadImage(image, folderName, fileName);
    }
}
