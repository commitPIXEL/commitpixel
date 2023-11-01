package com.ssafy.realrealfinal.pixelms.api.pixel.service;

public interface PixelService {

    void updateUsedPixel(Integer providerId);

    Integer getAvailableCredit(Integer providerId);

}
