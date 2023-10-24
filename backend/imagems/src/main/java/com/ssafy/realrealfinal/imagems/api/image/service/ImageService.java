package com.ssafy.realrealfinal.imagems.api.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void convertImage(MultipartFile file);

}
