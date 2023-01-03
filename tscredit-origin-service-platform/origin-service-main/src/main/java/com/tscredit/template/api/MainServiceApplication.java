package com.tscredit.template.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"com.tscredit"})
@MapperScan({"com.tscredit.*.dao","com.tscredit.*.mapper"})
public class MainServiceApplication extends SpringBootServletInitializer {


    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MainServiceApplication.class);
    }

    /**
     * 入口
     */
    public static void main(String[] args) {
        SpringApplication.run(MainServiceApplication.class, args);
    }

}
