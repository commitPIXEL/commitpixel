package com.ssafy.realrealfinal.imagems.common.util;

import com.ssafy.realrealfinal.imagems.common.exception.image.S3Exception;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Slf4j
@RequiredArgsConstructor
@Component
public class AwsS3Util {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    /**
     * Amazon S3에서 주어진 버킷과 파일 이름에 대한 URL 가져오기
     *
     * @param bucket   S3 버킷
     * @param fileName 파일 이름
     * @return 파일 URL
     */
    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    /**
     * Amazon S3에서 이미지 읽어오기
     *
     * @param fileName S3에서 읽어올 파일 이름
     * @return S3에서 읽은 BufferedImage 객체
     */
    public BufferedImage readImageFromS3(String fileName) {
        String s3Url = getS3(bucket, fileName);
        System.out.println(s3Url);
        try {
            URL url = new URL(s3Url);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000); // Set timeout as needed
            connection.setReadTimeout(10000); // Set timeout as needed

            try (InputStream inputStream = connection.getInputStream()) {
                return ImageIO.read(inputStream);
            }
        } catch (IOException e) {
            log.warn("readImageFromS3 mid: failed to fetch image from S3 - " + fileName);
            return null; // 이제 예외 발생 시 null을 반환합니다.
        }

    }

    /**
     * 이미지를 Amazon S3에 업로드 (scheduled로 자동화)
     *
     * @param image      업로드할 BufferedImage 객체
     * @param folderName S3에서 파일을 저장할 폴더 이름
     * @param fileName   S3에서 저장할 파일 이름
     */
    public void uploadImage(BufferedImage image, String folderName, String fileName) {
        File tempFile = null;
        try {
            // 이미지를 저장할 임시 파일 생성
            tempFile = File.createTempFile("temp_img", ".png");

            // BufferedImage 객체를 임시 파일에 쓰기
            ImageIO.write(image, "png", tempFile);

            // S3에서 객체의 키 정의 (folderName/fileName)
            String fileKey = folderName + "/" + fileName + ".png";

            // 파일을 S3에 업로드하고 공개 읽기 접근 권한을 부여
            amazonS3.putObject(new PutObjectRequest(bucket, fileKey, tempFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new S3Exception();
        } finally {
            // 임시 파일 정리
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }


}
