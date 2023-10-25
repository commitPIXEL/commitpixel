package com.ssafy.realrealfinal.imagems.api.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ImageService {

    byte[] convertImage(MultipartFile file, Integer type);

}
