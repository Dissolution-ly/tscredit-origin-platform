package com.tscredit.origin.oauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@EnableCaching
@EnableFeignClients(basePackages = "com.tscredit")
@ComponentScan("com.tscredit")
@MapperScan("com.tscredit.origin.oauth.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class OriginOauthApplication {

	public static void main(String[] args) {
		SpringApplication.run(OriginOauthApplication.class, args);
	}

}
