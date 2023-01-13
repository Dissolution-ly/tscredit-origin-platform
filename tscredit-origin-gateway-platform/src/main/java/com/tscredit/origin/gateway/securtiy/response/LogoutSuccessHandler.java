package com.tscredit.origin.gateway.securtiy.response;

import com.alibaba.fastjson.JSONObject;
import com.aurora.base.entity.response.Result;
import com.aurora.redis.config.RedisUtil;
import com.tscredit.origin.gateway.entity.Constants;
import com.tscredit.origin.gateway.securtiy.entity.User;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 退出成功
 */
@Component
public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {

    private final RedisUtil redisUtil;

    public LogoutSuccessHandler(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {

        try {
            if(authentication.isAuthenticated()){
                // 将 Token 加入黑名单
                User user = (User) authentication.getPrincipal();
                long exp = user.getJwtExp() - (System.currentTimeMillis() / 1000) + 20;
                redisUtil.set(Constants.TOKEN_BLACKLIST_PREFIX + authentication.getCredentials(), null, exp);
            }
        } catch (Exception ignored) {
        }

        // 响应退出成功
        return Mono.defer(() -> Mono.just(exchange.getExchange()
                .getResponse()).flatMap(response -> {
            DataBuffer dataBuffer;
            try {
                dataBuffer = response.bufferFactory()
                        .wrap(JSONObject.toJSONString(Result.success()).getBytes());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}