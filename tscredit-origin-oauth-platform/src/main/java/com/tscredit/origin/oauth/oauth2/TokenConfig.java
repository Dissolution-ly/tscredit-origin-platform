package com.tscredit.origin.oauth.oauth2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;


@Configuration
public class TokenConfig {

    @Bean
    public KeyPair keyPair() {
        //从classpath下的证书中获取秘钥对
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("origin.jks"), "origin-jks".toCharArray());
        return keyStoreKeyFactory.getKeyPair("origin", "origin-jks".toCharArray());
    }


    @Bean
    JwtAccessTokenConverter jwtAccessTokenConverter() {
        // JwtAccessTokenConverter 实现 用户信息 和 jwt的互相转换
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 对称密钥，资源服务器使用该密钥进行验证
        converter.setKeyPair(keyPair());
        return converter;
    }


    /**
     * Token 的存储方式
     */
    @Bean
    TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }
}