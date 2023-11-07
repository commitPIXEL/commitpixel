package com.ssafy.realrealfinal.pixelms.api.pixel.service;

import com.ssafy.realrealfinal.pixelms.api.pixel.request.AdditionalCreditReq;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.CreditRes;
import com.ssafy.realrealfinal.pixelms.api.pixel.response.PixelInfoRes;
import java.util.List;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

    byte[] redisToImage();

    String bufferedImageToBase64Image();

    void initCanvas();

    void updatePixelAndSendRank(Integer providerId, List pixelInfo);

    CreditRes updateAndSendCredit(AdditionalCreditReq additionalCreditRes);

    PixelInfoRes getUrlAndName(String index);
}
