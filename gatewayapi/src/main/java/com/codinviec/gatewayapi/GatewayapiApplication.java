package com.codinviec.gatewayapi;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayapiApplication {
	public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure()
                .directory("gatewayapi")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
        SpringApplication.run(GatewayapiApplication.class, args);
	}
}
