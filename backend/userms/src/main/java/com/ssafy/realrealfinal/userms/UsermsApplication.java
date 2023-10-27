package com.ssafy.realrealfinal.userms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@EnableJpaRepositories(basePackages = "com.ssafy.realrealfinal")
@SpringBootApplication
public class UsermsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsermsApplication.class, args);
    }

}
