package com.tscredit.origin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
@EnableFeignClients("com.tscredit.service.client")
public class OriginGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OriginGatewayApplication.class, args);
	}

}
