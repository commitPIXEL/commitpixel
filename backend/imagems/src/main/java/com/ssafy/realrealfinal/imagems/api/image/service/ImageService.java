package com.ssafy.realrealfinal.imagems.api.image.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

public interface ImageService {

    byte[] convertImage(String accessToken, MultipartFile file, Integer type);

    byte[] getGIF() throws IOException;
}
