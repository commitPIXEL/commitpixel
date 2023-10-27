package com.ssafy.realrealfinal.authms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableFeignClients
@EnableDiscoveryClient
@EnableJpaRepositories(basePackages = "com.ssafy.realrealfinal")
@SpringBootApplication
public class AuthmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthmsApplication.class, args);
    }

}
