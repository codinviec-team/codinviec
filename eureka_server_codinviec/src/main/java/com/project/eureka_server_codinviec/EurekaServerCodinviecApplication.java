package com.project.eureka_server_codinviec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerCodinviecApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerCodinviecApplication.class, args);
	}

}
