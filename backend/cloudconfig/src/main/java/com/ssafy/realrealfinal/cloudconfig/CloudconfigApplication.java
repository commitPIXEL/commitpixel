package com.ssafy.realrealfinal.cloudconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class CloudconfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudconfigApplication.class, args);
	}

}
