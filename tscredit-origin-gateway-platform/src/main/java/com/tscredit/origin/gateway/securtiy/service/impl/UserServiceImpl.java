package com.tscredit.origin.gateway.securtiy.service.impl;

import com.alibaba.nacos.common.utils.MapUtil;
import com.aurora.base.entity.response.Result;
import com.aurora.base.enums.ResultCodeEnum;
import com.aurora.base.exception.LogicException;
import com.aurora.base.util.JwtUtil;
import com.aurora.redis.config.RedisUtil;
import com.tscredit.origin.gateway.entity.Constants;
import com.tscredit.origin.gateway.securtiy.entity.User;
import com.tscredit.origin.gateway.securtiy.service.UserService;
import com.tscredit.service.client.user.dto.UserDTO;
import com.tscredit.service.client.user.feign.UserFeign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service(value = "userDetailsService")
@RefreshScope
public class UserServiceImpl implements UserService {


    @Lazy
    @Autowired
    private UserFeign userFeign;
    private final RedisUtil redisUtil;

    // 十小时(秒为单位)
    @Value("${user.jwt.timeout.token:36000}")
    private int jwtTokenExpired;

    // 七天(秒为单位)
    @Value("${user.jwt.timeout.refreshToken:604800}")
    private int jwtTokenRefreshExpired;

    @Value("${user.jwt.key}")
    private String jwtKey;

    public UserServiceImpl(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        // WebFlux异步调用，同步会报错
        Result<UserDTO> result;
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            result = executorService.submit(() -> userFeign.getByName(username)).get();
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        if (result.getCode() != 20000 || result.getData() == null) {
            return Mono.empty();
        }
        User user = new User();
        BeanUtils.copyProperties(result.getData(), user);
        return Mono.just(user);
    }

    @Override
    public User findByUserId(String id) {
        // WebFlux异步调用，同步会报错
        Result<UserDTO> result = null;
        try {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            result = executorService.submit(() -> userFeign.getById(id)).get();
            executorService.shutdown();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }


        if (result.getCode() != 20000 || result.getData() == null) {
            return null;
        }
        User user = new User();
        BeanUtils.copyProperties(result.getData(), user);
        return user;
    }

    @Override
    public void responseToken(HttpHeaders headers, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        User user = (User) usernamePasswordAuthenticationToken.getPrincipal();
        long exp = user.getJwtExp() - (System.currentTimeMillis() / 1000);
        if(exp < jwtTokenExpired/2){
            Map<String, String> tokenMap = generateToken(user);
            headers.add("token", tokenMap.get("token"));
        }
    }

    @Override
    public Map<String, String> generateToken(User user) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("id", user.getId());
        map.put("username", user.getUsername());
        map.put("authorities", user.getAuthorities());

        // 生成 Jwt Token
        Map<String, String> tokenMap = new HashMap<>(2);
        tokenMap.put("token", JwtUtil.encode(map, jwtKey, jwtTokenExpired));
        tokenMap.put("refreshToken", JwtUtil.encode(map, jwtKey + ":refresh", jwtTokenRefreshExpired));
        return tokenMap;
    }


    @Override
    public UsernamePasswordAuthenticationToken decodeToken(String token) {

        // 解密 jwt
        Map<String, Object> decode = JwtUtil.decode(token, jwtKey);
        if (MapUtil.isEmpty(decode) || redisUtil.hasKey(Constants.TOKEN_BLACKLIST_PREFIX + token)) {
            throw LogicException.errorMessage(ResultCodeEnum.USER_TOKEN_LOSE);
        }
        // 获取用户信息
        User user = findByUserId(decode.get("id").toString());
        if (user == null) {
            throw LogicException.errorMessage(ResultCodeEnum.USER_RE_LOGIN);
        }
        user.setJwtExp((int) decode.get("exp"));
        Collection<? extends GrantedAuthority> roles = (Collection<? extends GrantedAuthority>) decode.get("roles");
        return new UsernamePasswordAuthenticationToken(user, token, roles);
    }


    @Override
    public Map<String, String> generateTokenByRefresh(String refreshToken) {
        Map<String, Object> decode = JwtUtil.decode(refreshToken, jwtKey + ":refresh");
        if (MapUtil.isEmpty(decode) || redisUtil.hasKey(Constants.REFRESHTOKEN_BLACKLIST_PREFIX + refreshToken)) {
            throw LogicException.errorMessage(ResultCodeEnum.REQ_PARAM_ERROR);
        }

        User user = findByUserId(decode.get("id").toString());
        if (user == null) {
            throw LogicException.errorMessage(ResultCodeEnum.USER_RE_LOGIN);
        }

        long exp = (int) decode.get("exp") - (System.currentTimeMillis() / 1000) + 20;
        redisUtil.set(Constants.REFRESHTOKEN_BLACKLIST_PREFIX + refreshToken, null, exp);
        return generateToken(user);
    }
}
