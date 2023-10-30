package com.ssafy.realrealfinal.imagems.api.image.controller;

import com.ssafy.realrealfinal.imagems.api.image.service.ImageService;
import com.ssafy.realrealfinal.imagems.common.util.TimelapseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/image")
@RestController
public class ImageController {

    private final ImageService imageService;
    private final TimelapseUtil timelapseUtil;

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

    @PostMapping("/test")
    public ResponseEntity<?> test() throws Exception {
        // 첫 번째 이미지를 읽고 BufferedImage 객체로 로드.
        BufferedImage first = ImageIO.read(new File("C:\\Users\\SSAFY\\Downloads\\1.png"));
        first = convertToIndexed(first); // 색상 모델을 조절.

        // GIF 파일을 저장할 출력 스트림을 생성.
        ImageOutputStream output = new FileImageOutputStream(new File("/tmp/example.gif"));

        // GifSequenceWriter 객체를 생성. 이 객체는 GIF 이미지 시퀀스를 작성하는 데 사용됨.
        TimelapseUtil writer = new TimelapseUtil(output, first.getType(), 250, true);

        // 첫 번째 이미지를 GIF 시퀀스에 작성.
        writer.writeToSequence(first);

        // 작성할 추가 이미지의 파일 객체 배열을 생성.
        File[] images = new File[]{
            new File("C:\\Users\\SSAFY\\Downloads\\1.png"),
            new File("C:\\Users\\SSAFY\\Downloads\\2.png"),
            new File("C:\\Users\\SSAFY\\Downloads\\3.png"),
        };

        // 각 이미지에 대해
        for (File image : images) {
            // 이미지를 읽고 BufferedImage 객체로 로드.
            BufferedImage next = ImageIO.read(image);
            next = convertToIndexed(next); // 색상 모델을 조절.

            // 해당 이미지를 GIF 시퀀스에 작성.
            writer.writeToSequence(next);
        }

        // writer와 출력 스트림을 닫아 리소스를 해제.
        writer.close();
        output.close();

        return ResponseEntity.ok().build();
    }

    public static BufferedImage convertToIndexed(BufferedImage src) {
        BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(),
            BufferedImage.TYPE_BYTE_INDEXED);
        dest.getGraphics().drawImage(src, 0, 0, null);
        return dest;
    }
}
