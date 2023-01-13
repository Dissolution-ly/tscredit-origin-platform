package com.tscredit.origin.gateway.securtiy.service;


import com.tscredit.origin.gateway.securtiy.entity.User;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import java.util.Map;

public interface UserService extends ReactiveUserDetailsService {

    public Map<String, String> generateToken(User user);

    public Map<String, String> generateTokenByRefresh(String refreshToken);

    public UsernamePasswordAuthenticationToken decodeToken(String token);

    public User findByUserId(String id);

    void responseToken(HttpHeaders headers, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken);

}
