package com.ssafy.realrealfinal.imagems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.ssafy.realrealfinal")
@SpringBootApplication
public class ImagemsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImagemsApplication.class, args);
    }

}
