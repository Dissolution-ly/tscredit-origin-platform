package com.tscredit.origin.gateway.securtiy.response;

import com.alibaba.fastjson.JSONObject;
import com.aurora.base.entity.response.Result;
import com.aurora.base.enums.ResultCodeEnum;
import com.tscredit.origin.gateway.securtiy.MyAuthenticationException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 登录失败
 */
@Component
public class AuthenticationFailure implements ServerAuthenticationFailureHandler {
    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        return Mono.defer(() -> Mono.just(webFilterExchange.getExchange()
                .getResponse()).flatMap(response -> {
            DataBufferFactory dataBufferFactory = response.bufferFactory();

            Result<Object> result = Result.result(ResultCodeEnum.USER_PASSWORD);
            if (exception instanceof BadCredentialsException) {
                result = Result.error(exception.getMessage());
            } else if (exception instanceof DisabledException || exception instanceof LockedException) {
                result = Result.result(ResultCodeEnum.USER_CLOSE);
            } else if (exception instanceof AccountExpiredException) {
                result = Result.result(ResultCodeEnum.USER_OVERDUE);
            } else if (exception instanceof AuthenticationServiceException) {
                result = Result.result(ResultCodeEnum.USER_NULL);
            } else if (exception instanceof MyAuthenticationException) {
                MyAuthenticationException exception1 = (MyAuthenticationException) exception;
                result = Result.error(exception1.getMessage()).setCode(Integer.parseInt(exception1.getCode()));
            }

            DataBuffer dataBuffer = dataBufferFactory.wrap(JSONObject.toJSONString(result).getBytes());
            return response.writeWith(Mono.just(dataBuffer));
        }));
    }
}