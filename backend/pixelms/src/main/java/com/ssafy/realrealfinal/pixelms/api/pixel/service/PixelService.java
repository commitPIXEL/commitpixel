package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import java.awt.image.BufferedImage;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    BufferedImage redisToImage();

    String bufferedImageToBase64Image();
}
