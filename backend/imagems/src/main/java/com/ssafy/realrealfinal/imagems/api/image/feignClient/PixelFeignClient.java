package com.ssafy.realrealfinal.imagems.api.image.feignClient;


import java.awt.image.BufferedImage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "pixel", url = "${feign.url}")
public interface PixelFeignClient {

    @GetMapping("pixel/image")
    byte[] getImage();
}
