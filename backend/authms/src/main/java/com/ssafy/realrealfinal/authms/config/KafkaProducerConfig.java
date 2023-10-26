//package com.ssafy.realrealfinal.authms.config;
//
//import com.ssafy.realrealfinal.authms.api.auth.dto.OauthUserDto;
//import java.util.HashMap;
//import java.util.Map;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//
//@Configuration
//// 이 어노테이션은 이 클래스가 스프링의 구성 클래스임을 나타냅니다. 이 클래스는 Bean을 정의하고, 이들 Bean이 어떻게 연결되어 있는지를 설정합니다.
//public class KafkaProducerConfig {
//
//
//    @Bean // 이 어노테이션은 스프링 컨테이너에 의해 관리되는 객체, 즉 Bean을 정의합니다. 이 Bean은 다른 Bean에서 주입받아 사용할 수 있습니다.
//    public ProducerFactory<String, OauthUserDto> producerFactory() { // Kafka의 ProducerFactory를 구성하는 메서드입니다. ProducerFactory는 Kafka에 메시지를 보내는 Producer들을 생성하는 팩토리입니다.
//        Map<String, Object> config = new HashMap<>(); // Kafka producer 설정을 저장하는 Map을 생성합니다. 이 Map에는 Kafka 서버의 위치, 데이터의 직렬화 방법 등이 설정됩니다.
//        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//            "localhost:9092"); // Kafka 서버의 위치를 설정합니다. 여기서는 localhost의 9092포트로 설정되어 있습니다.
//        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//            StringSerializer.class);  // 메시지의 키를 어떻게 직렬화할지 설정합니다. 여기서는 문자열을 직렬화하는 StringSerializer를 사용합니다.
//        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//            JsonSerializer.class); // 메시지의 값을 어떻게 직렬화할지 설정합니다. 여기서도 문자열을 직렬화하는 StringSerializer를 사용합니다.
//
//        return new DefaultKafkaProducerFactory<>(
//            config);  // 설정을 바탕으로 DefaultKafkaProducerFactory를 생성하고 반환합니다. 이 팩토리는 실제 메시지를 보내는 Kafka Producer를 생성합니다.
//    }
//
//    @Bean   // 다시 한 번 Bean을 정의합니다. 이 Bean은 위에서 정의한 producerFactory를 사용하여 KafkaTemplate을 생성합니다.
//    public KafkaTemplate<String, OauthUserDto> kafkaTemplate() { // KafkaTemplate은 Kafka로 메시지를 보내기 위한 템플릿입니다. 이를 사용하여 메시지를 쉽게 보낼 수 있습니다.
//        return new KafkaTemplate<>(
//            producerFactory()); // producerFactory를 사용하여 KafkaTemplate를 생성하고 반환합니다. 이 KafkaTemplate은 다른 곳에서 메시지를 보내기 위해 사용됩니다.
//    }
//
//}
