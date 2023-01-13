package com.tscredit.origin.gateway.action;


import com.tscredit.origin.gateway.securtiy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@RestController(value = "/loginBefor")
@Tag(name = "登录", description = "LoginAction")
public class LoginAction {

    private final UserService userService;

    public LoginAction(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "刷新 token")
    @PostMapping(value = "/refreshToken")
    public Mono<ResponseEntity<Map<String, String>>> refreshToken(@RequestBody String refreshToken) {
        return Mono.just((new ResponseEntity<>(userService.generateTokenByRefresh(refreshToken), HttpStatus.OK)));
    }
}
