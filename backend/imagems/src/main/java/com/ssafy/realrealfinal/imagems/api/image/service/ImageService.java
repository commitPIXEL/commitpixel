package com.ssafy.realrealfinal.imagems.api.image.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    byte[] convertImage(MultipartFile file, Integer type);

    byte[] getGif();

    byte[] getAllGif();
}
