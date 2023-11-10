package com.ssafy.realrealfinal.imagems.api.image.service;


import com.ssafy.realrealfinal.imagems.common.exception.image.GifConvertException;
import com.ssafy.realrealfinal.imagems.common.exception.image.ImageConvertException;
import com.ssafy.realrealfinal.imagems.common.util.AwsS3Util;
import com.ssafy.realrealfinal.imagems.common.util.GifImageUtil;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;
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

    private final AwsS3Util awsS3Util;

    /**
     * 이미지 픽셀화 메서드
     *
     * @param file 픽셀 시도 그림
     * @param type 1,2 (크기
     * @return 픽셀 이미지
     */
    @Override
    public byte[] convertImage(MultipartFile file, Integer type) {

        log.info("convertImage start: " + type);

        int pixelSize = getPixelSize(type);
        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            // 이미지를 작게 리사이징
            BufferedImage resizedImage = Scalr.resize(originalImage, Scalr.Method.SPEED, pixelSize);
            // 다시 원래의 크기로 확대
            BufferedImage pixelatedImage = Scalr.resize(resizedImage, Scalr.Method.SPEED,
                originalImage.getWidth());
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

    /**
     * type이 1이면 64픽셀, 2면 32픽셀로 변환
     *
     * @param type 1,2
     * @return 픽셀 값
     */
    private int getPixelSize(Integer type) {
        if (type == 1) {
            return 64;
        } else {
            return 32;
        }
    }

    /**
     * 24시간 동안의 48개의 이미지 데이터 받아서 gif로 만듦
     *
     * @return GIF로 전환할 정보
     */
    @Override
    public byte[] getGif() {
        log.info("getGIF start");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursAgo = now.minusHours(24);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startFolder = twentyFourHoursAgo.format(formatter);
        String endFolder = now.format(formatter);
        //startFolder에서는 이 시간부터, endFolder에서는 이 시간까지 file 가져오기.
        LocalDateTime startTime = getRoundedHoursAgo(now, 24);

        String[] filePaths = generateFilePaths(startFolder, endFolder, startTime);

        // 첫 번째 이미지를 읽고 BufferedImage 객체로 로드.
        try {
            String fullPath = startFolder + "/" + startTime + ".png";
            BufferedImage first = awsS3Util.readImageFromS3(fullPath);
            first = convertToIndexed(first); // 색상 모델을 조절.

            // ByteArrayOutputStream을 생성. 이미지 데이터를 메모리에 저장.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageOutputStream output = new MemoryCacheImageOutputStream(baos);

            // GifImageUtil 객체를 생성. 이 객체는 GIF 이미지 시퀀스를 작성하는 데 사용됨.
            GifImageUtil writer = new GifImageUtil(output, first.getType(), 250, true);

            // 첫 번째 이미지를 GIF 시퀀스에 작성.
            writer.writeToSequence(first);

            //전환해서 BufferedImage[]로 받도록 구현해야함.
            for (String fileName : filePaths) {
                try {
                    BufferedImage next = awsS3Util.readImageFromS3(fileName);
                    if (next != null) { // null 체크 추가
                        next = convertToIndexed(next); // 색상 모델을 조절.
                        writer.writeToSequence(next); // 해당 이미지를 GIF 시퀀스에 작성.
                    } else {
                        log.warn("Skipping a frame due to failed image retrieval: " + fileName);
                    }
                } catch (IOException e) {
                    log.error("Error reading image from S3: " + fileName, e);
                    // 이미지 읽기 실패 시 로깅하고 넘어감. 필요에 따라 다른 처리를 할 수도 있음.
                }
            }

            // writer와 출력 스트림을 닫아 리소스를 해제.
            writer.close();
            output.close();

            byte[] gifBytes = baos.toByteArray(); // ByteArrayOutputStream에서 바이트 배열을 얻음.
            log.info("getGIF end: SUCCESS");
            return gifBytes;
        } catch (Exception e) {
            throw new GifConvertException();
        }
    }


    /**
     * BufferedImage를 INDEXED 이미지로 변환
     *
     * @param src 변환할 원본 BufferedImage
     * @return INDEXED BufferedImage로 변환된 이미지
     */
    public static BufferedImage convertToIndexed(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
            BufferedImage.TYPE_BYTE_INDEXED);
        dest.getGraphics().drawImage(src, 0, 0, null);
        return dest;
    }

    /**
     * 기준 시간으로부터 정해진 시간 이전을 10분 단위로 반올림
     *
     * @param dateTime 기준 LocalDateTime
     * @param hoursAgo 기준 시간으로부터 이전 시간
     * @return 반올림된 시간
     */
    public static LocalDateTime getRoundedHoursAgo(LocalDateTime dateTime, int hoursAgo) {
        LocalDateTime pastDateTime = dateTime.minusHours(hoursAgo);

        int minutes = pastDateTime.getMinute();
        int roundedMinutes = (minutes / 10) * 10; // 10분 단위로 반올림
        LocalDateTime roundedDateTime = pastDateTime.withMinute(roundedMinutes).withSecond(0)
            .withNano(0);

        // 새벽 1시부터 8시까지는 제외
        if (roundedDateTime.getHour() >= 1 && roundedDateTime.getHour() < 8) {
            roundedDateTime = roundedDateTime.withHour(8).withMinute(0);
        }

        return roundedDateTime;

    }

    /**
     * 시작 폴더와 종료 폴더에 대해 파일 경로를 생성
     *
     * @param startFolder 시작 폴더의 이름
     * @param endFolder   종료 폴더의 이름
     * @param startTime   시작 시간
     * @return 생성된 파일 경로의 배열
     */
    public static String[] generateFilePaths(String startFolder, String endFolder, LocalDateTime startTime) {
        List<String> filePaths = new ArrayList<>();
        LocalDateTime time = startTime;

        while (!time.toLocalDate().isAfter(LocalDate.parse(endFolder))) {
            if (!(time.getHour() >= 1 && time.getHour() < 8)) {
                filePaths.add(createFilePath(time.getHour() < 24 ? startFolder : endFolder, time));
            }
            time = time.plusMinutes(10);
        }

        return filePaths.toArray(new String[0]);
    }

    /**
     * 폴더와 시간을 기반으로 파일 경로 생성
     *
     * @param folder 경로를 생성할 폴더의 이름
     * @param dateTime   경로에 포함할 시간
     * @return 생성된 파일 경로 문자열
     */
    private static String createFilePath(String folder, LocalDateTime dateTime) {
        return String.format("%s/%02d.%02d.png", folder, dateTime.getHour(), dateTime.getMinute());
    }

}
