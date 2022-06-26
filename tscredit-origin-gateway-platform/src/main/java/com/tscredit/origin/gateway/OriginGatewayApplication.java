package com.tscredit.origin.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@ComponentScan("com.tscredit")
@SpringBootApplication
public class OriginGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(OriginGatewayApplication.class, args);
	}

}
