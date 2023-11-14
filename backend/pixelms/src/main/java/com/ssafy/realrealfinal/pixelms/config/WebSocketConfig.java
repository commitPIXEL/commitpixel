package com.ssafy.realrealfinal.pixelms.config;


import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {

    @Bean
    public SocketIOServer socketIoServer() {
        //Configuration 충돌 해결을 위해 풀패키지 이름 사용
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");
        config.setPort(3001);
        config.setOrigin("https://commitpixel.com, https://dev.commitpixel.com, http://localhost");
        // 타임아웃 설정
        config.setPingTimeout(600000);  // 600초
        config.setPingInterval(25000); // 25초
        return new SocketIOServer(config);
    }

    @Bean
    public SpringAnnotationScanner springAnnotationScanner(SocketIOServer socketIOServer) {
        return new SpringAnnotationScanner(socketIOServer);
    }
}