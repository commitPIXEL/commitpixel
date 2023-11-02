package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import java.util.List;
import java.awt.image.BufferedImage;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    void updatePixelRedisAndSendRank(Integer providerId, List pixelInfo);

    BufferedImage redisToImage();

    String bufferedImageToBase64Image();

}
