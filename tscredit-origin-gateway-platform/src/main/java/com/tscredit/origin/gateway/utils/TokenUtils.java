//package com.tscredit.origin.gateway.utils;
//
//
//import com.aurora.base.util.JsonUtils;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.nio.charset.StandardCharsets;
//
//public class TokenUtils {
//    /**
//     * 获取token
//     */
//    public static String getToken(ServerWebExchange exchange) {
//        String tokenStr = exchange.getRequest().getHeaders().getFirst("Authorization");
//        if (StringUtils.isEmpty(tokenStr)) {
//            return null;
//        }
//        String token = tokenStr.split(" ")[1];
//        if (StringUtils.isEmpty(token)) {
//            return null;
//        }
//        return token;
//    }
//
//
//    public static Mono<Void> buildReturnMono(Object json, ServerWebExchange exchange) {
//        ServerHttpResponse response = exchange.getResponse();
//        byte[] bits = JsonUtils.convertObject2JsonLogicException(json).getBytes(StandardCharsets.UTF_8);
//        DataBuffer buffer = response.bufferFactory().wrap(bits);
//        response.setStatusCode(HttpStatus.UNAUTHORIZED);
//        //指定编码，否则在浏览器中会中文乱码
//        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
//        return response.writeWith(Mono.just(buffer));
//    }
//}
