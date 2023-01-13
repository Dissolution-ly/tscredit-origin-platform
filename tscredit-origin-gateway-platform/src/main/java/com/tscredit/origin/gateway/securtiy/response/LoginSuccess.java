package com.tscredit.origin.gateway.securtiy.response;

import com.alibaba.fastjson.JSONObject;
import com.aurora.base.entity.response.Result;
import com.tscredit.origin.gateway.securtiy.entity.User;
import com.tscredit.origin.gateway.securtiy.service.UserService;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 登录成功
 */


@Component
@RefreshScope
public class LoginSuccess implements ServerAuthenticationSuccessHandler {

    private final UserService userService;

    public LoginSuccess(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange().getResponse()).flatMap(response -> {
            // 要保证所有登录方式的 `Authentication` 都能够获取到 User 对象
            User userDetails = (User) authentication.getPrincipal();

            // 响应 Token
            DataBufferFactory dataBufferFactory = response.bufferFactory();
            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(Result.success(userService.generateToken(userDetails))).getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}