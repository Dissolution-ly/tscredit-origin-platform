package com.tscredit.origin.gateway.config;


import com.tscredit.origin.gateway.entity.AuthUrlWhiteListProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;

@Configuration
public class Test {

    @Bean
    @ConditionalOnMissingBean(name = "authUrlWhiteListProperties2")
    public AuthUrlWhiteListProperties authUrlWhiteListProperties2(){
        AuthUrlWhiteListProperties authUrlWhiteListProperties = new AuthUrlWhiteListProperties();
        authUrlWhiteListProperties.setAuthUrls(new ArrayList<>());
        return authUrlWhiteListProperties;
    }

    @Bean
    @ConditionalOnMissingBean(name = "authUrlWhiteListProperties")
    public AuthUrlWhiteListProperties authUrlWhiteListProperties(){
        AuthUrlWhiteListProperties authUrlWhiteListProperties = new AuthUrlWhiteListProperties();
        authUrlWhiteListProperties.setAuthUrls(new ArrayList<>());
        return authUrlWhiteListProperties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(RedisTemplate.class)
    public StringBuffer authUrlWhiteListProperties2(RedisTemplate redisTemplate){
        return new StringBuffer();
    }


}
