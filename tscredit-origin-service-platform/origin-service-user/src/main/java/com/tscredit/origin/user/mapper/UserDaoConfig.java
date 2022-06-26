package com.tscredit.origin.user.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.tscredit.template.user.dao")
public class UserDaoConfig {
}
