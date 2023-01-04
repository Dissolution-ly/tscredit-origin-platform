package com.tscredit.origin.main.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {
    /**
     * 请求失败后的重试配置
     */
    @Bean
    public Retryer feignRetryer() {
        // 重试间隔100ms，最大重试间隔时间为1秒，重试次数为3次
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1L), 3);
    }
}