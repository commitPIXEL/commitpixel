package com.ssafy.realrealfinal.imagems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ImagemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagemsApplication.class, args);
    }

}
