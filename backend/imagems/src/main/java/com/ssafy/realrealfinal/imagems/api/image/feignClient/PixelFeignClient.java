package com.ssafy.realrealfinal.imagems.api.image.feignClient;


import java.awt.image.BufferedImage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "pixel", url = "http://k9a709.p.ssafy.io:8183")
public interface PixelFeignClient {

    @GetMapping("pixel/image")
    byte[] getImage();
}
