package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.dto.AdditionalCreditDto;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;

import java.util.List;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    void updatePixelRedisAndSendRank(Integer providerId, List pixelInfo);

    CreditRes updateAndSendCredit(AdditionalCreditDto additionalCreditRes);

    PixelInfoRes getUrlAndName(String index);

    byte[] redisToImage();

    String bufferedImageToBase64Image();

}
