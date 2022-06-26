package com.tscredit.origin.user.lua;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.List;

@Configuration
public class RedisLua {

    @Bean
    public DefaultRedisScript<List> removeAll(){
        DefaultRedisScript<List>  redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/removeAll.lua")));
        redisScript.setResultType(List.class);
        return redisScript;
    }


    @Bean
    public DefaultRedisScript<Object> resetZero(){
        DefaultRedisScript<Object>  redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("redis/resetZero.lua")));
        return redisScript;
    }
}
