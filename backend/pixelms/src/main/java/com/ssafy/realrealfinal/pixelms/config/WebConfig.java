package com.ssafy.realrealfinal.pixelms.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry
            .addMapping("/pixel/**")  // 허용할 URL 패턴 설정
            .allowedOrigins("http://localhost:3000", "https://dev.commitpixel.com")  // 허용할 오리진(도메인) 설정
            .allowedMethods("GET", "POST", "PUT", "PATCH", "OPTIONS", "DELETE")  // 허용할 HTTP 메소드 설정
            .allowedHeaders("*")  // 허용할 헤더 설정
            .allowCredentials(true);
    }
}

