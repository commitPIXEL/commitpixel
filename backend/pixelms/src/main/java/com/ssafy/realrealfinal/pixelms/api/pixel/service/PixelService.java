package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.dto.AdditionalCreditDto;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;
import java.awt.image.BufferedImage;
import java.util.List;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    byte[] redisToImage();

    String bufferedImageToBase64Image();

    public void test();

    public void updatePixelRedisAndSendRank(Integer providerId, List pixelInfo);

    public CreditRes updateAndSendCredit(AdditionalCreditDto additionalCreditRes);

    public PixelInfoRes getUrlAndName(String index);
}
