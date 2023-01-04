package com.tscredit.service.user.client.config;


import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.tscredit.service.user.client")
public class OpenFeignConfig {
}
