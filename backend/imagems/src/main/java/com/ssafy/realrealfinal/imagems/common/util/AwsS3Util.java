package com.ssafy.realrealfinal.imagems.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
@Component
public class AwsS3Util {

    //    https://develop-writing.tistory.com/128 //-> 다운로드 파일
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

//    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
//        File file = convertMultipartFileToFile(multipartFile)
//            .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File convert fail"));
//
//        return upload(file, dirName);
//    }
//
//    private String upload(File file, String dirName) {
//        String key = randomFileName(file, dirName);
//        String path = putS3(file, key);
//        removeFile(file);
//
//        return path;
//    }
//
//    private String randomFileName(File file, String dirName) {
//        return dirName + "/" + UUID.randomUUID() + file.getName();
//    }
//
//    private String putS3(File uploadFile, String fileName) {
//        amazonS3.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
//            .withCannedAcl(CannedAccessControlList.PublicRead));
//        return getS3(bucket, fileName);
//    }

    private String getS3(String bucket, String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    public BufferedImage readImageFromS3(String fileName) throws IOException {
        String s3Url = getS3(bucket, fileName);
        System.out.println(s3Url);
        URL url = new URL(s3Url);
        URLConnection connection = url.openConnection();
        connection.setConnectTimeout(10000); // Set timeout as needed
        connection.setReadTimeout(10000); // Set timeout as needed

        try (InputStream inputStream = connection.getInputStream()) {
            return ImageIO.read(inputStream);
        }
    }

//
//    private void removeFile(File file) {
//        file.delete();
//    }
//
//    public Optional<File> convertMultipartFileToFile(MultipartFile multipartFile)
//        throws IOException {
//        File file = new File(
//            System.getProperty("user.dir") + "/" + multipartFile.getOriginalFilename());
//
//        if (file.createNewFile()) {
//            try (FileOutputStream fos = new FileOutputStream(file)) {
//                fos.write(multipartFile.getBytes());
//            }
//            return Optional.of(file);
//        }
//        return Optional.empty();
//    }


}
