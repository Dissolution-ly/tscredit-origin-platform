//package com.tscredit.origin.gateway.config.oauth2.resource;
//
//import com.aurora.base.constant.ErrorMessage;
//import com.aurora.base.entity.response.ActionMessage;
//import com.aurora.base.util.JsonUtils;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
///**
// * 自定义返回结果：没有 token 或 过期 时
// *
// */
//@Component
//public class RestAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
//  @Override
//  public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
//    ServerHttpResponse response = exchange.getResponse();
//    response.setStatusCode(HttpStatus.UNAUTHORIZED);
//    response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//    ActionMessage data = ActionMessage.error(ErrorMessage.USER_NO_LOGIN).data(e.getMessage());
//    String body = JsonUtils.convertObject2JsonLogicException(data);
//    DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
//    return response.writeWith(Mono.just(buffer));
//  }
//}
