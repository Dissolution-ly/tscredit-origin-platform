package com.tscredit.origin.gateway.securtiy.jwt;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.aurora.redis.config.RedisUtil;
import com.tscredit.origin.gateway.securtiy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Component
@RefreshScope
public class JwtSecurityContextRepository implements ServerSecurityContextRepository {

    public final static String TOKEN_HEADER = "Authorization";

    public final static String BEARER = "Bearer ";

    @Autowired
    private UserService userService;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        List<String> headers = request.getHeaders().get(TOKEN_HEADER);
        if (!CollectionUtils.isEmpty(headers)) {
            String authorization = headers.get(0);
            if (StringUtils.isNotEmpty(authorization)) {
                String token = authorization.substring(BEARER.length());
                if (StringUtils.isNotEmpty(token)) {
                    try {
                        // 解密 jwt
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = userService.decodeToken(token);
                        if(usernamePasswordAuthenticationToken != null){
                            userService.responseToken(exchange.getResponse().getHeaders(),usernamePasswordAuthenticationToken);
                            return Mono.just(usernamePasswordAuthenticationToken).map(SecurityContextImpl::new);
                        }
                    } catch (Exception e) {
                        log.error(ExceptionUtils.getStackTrace(e));
                        return Mono.empty();
                    }
                }
            }
        }

        return Mono.empty();
    }
}