package com.ssafy.realrealfinal.authms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.ssafy.realrealfinal")
@SpringBootApplication
public class AuthmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthmsApplication.class, args);
    }

}
