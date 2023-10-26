//package com.ssafy.realrealfinal.authms.config;
//
//import com.ssafy.realrealfinal.authms.api.auth.dto.OauthUserDto;
//import org.springframework.kafka.core.KafkaTemplate; // KafkaTemplate 클래스를 사용하기 위해 임포트합니다. KafkaTemplate은 Kafka에 메시지를 보내는 데 사용되는 클래스입니다.
//import org.springframework.stereotype.Component; // Component 어노테이션을 사용하기 위해 임포트합니다. Component 어노테이션은 이 클래스를 스프링 컨테이너가 관리하는 빈으로 등록합니다.
//
//@Component  // 이 클래스를 스프링 컨테이너가 관리하는 빈으로 선언합니다. 이를 통해 다른 클래스에서 자동 주입을 받아 사용할 수 있게 됩니다.
//public class KafkaProducer { // KafkaProducer 클래스를 정의합니다. 이 클래스는 Kafka에 메시지를 보내는 기능을 담당합니다.
//
//    private final KafkaTemplate<String, OauthUserDto> kafkaTemplate; // <String, OauthUserDto>로 변경
//
//    public KafkaProducer(KafkaTemplate<String, OauthUserDto> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void create(OauthUserDto oauthUser) { // OauthUserDto 객체를 파라미터로 받음
//        kafkaTemplate.send("userms", oauthUser); // oauthUser 객체를 보냄
//    }
//} // 클래스의 끝입니다.
