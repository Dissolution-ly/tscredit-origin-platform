package com.tscredit.origin.oauth.securtiy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurtiyConfig {
/**
     * Spring Security 过滤器链 - 身份验证
     */
    @Bean 
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
        throws Exception {
        http
            // 所有请求都需要进行登录验证
            .authorizeHttpRequests((authorize) -> authorize
                                   .anyRequest().authenticated()
                                  )
            // 抛出未登录异常时，重定向到登录页
            .formLogin(Customizer.withDefaults());

        return http.build();
    }


    /**
     * UserDetailsService 用户查询 Service (用于登录)
     */
    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
            .username("admin")
            .password("admin")
            .passwordEncoder(passwordEncoder()::encode)
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }


    /**
     * 密码加密方案
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        // 不对密码进行加密，Security 5.0 前默认
        // return NoOpPasswordEncoder.getInstance();

        // BCryptPasswordEncoder [BCrypt强哈希函数,官方推荐,自带盐值]
        // 密钥迭代次数为 2^strength(默认10)，还可以指定 BCrypt 版本、SecureRandom 实例
        // return new BCryptPasswordEncoder(10);

        // 采用委派密码编码器 (将允许数据库中不同用户密码使用不同加密方式，以更新密码加密方式)
        Map<String,PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("sha256", new StandardPasswordEncoder());

        DelegatingPasswordEncoder encoder = new DelegatingPasswordEncoder("bcrypt", encoders);
        // 默认情况下，如果  为null会抛出异常，可以通过设置默认密码编码器，在匹配不到时使用
        encoder.setDefaultPasswordEncoderForMatches(new SCryptPasswordEncoder());
        return encoder;

    }

    public static void main(String[] args) {
        if(null instanceof String){
            System.out.println("a1");
        }
    }
}