package com.tscredit.origin.gateway.securtiy;


import com.alibaba.nacos.common.utils.HttpMethod;
import com.tscredit.origin.gateway.securtiy.jwt.JwtSecurityContextRepository;
import com.tscredit.origin.gateway.securtiy.response.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.stream.Collectors;

import static org.springframework.security.config.Customizer.withDefaults;


/**
 * @author lixuanyu
 */
@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    private final DefaultAccessDecisionManager defaultAccessDecisionManager;
    private final JwtSecurityContextRepository jwtSecurityContextRepository;
    private final LoginSuccess loginSuccess;
    private final AuthenticationFailure authenticationFailure;
    private final LogoutSuccessHandler logoutSuccessHandler;

    private static final String[] AUTH_WHITELIST = {
            // 登录前的 二维码、验证码、 等请求
            "/loginBefor/**", "/refreshToken",
            // 跳转的错误页面
            "/error*/**",
            // -- swagger ui
            "/doc.html", "/favicon.ico", "/swagger-ui/*", "/swagger-resources/**",
            "/v2/api-docs", "/v3/api-docs", "/webjars/**"
    };

    public SecurityConfig(DefaultAccessDecisionManager defaultAccessDecisionManager, JwtSecurityContextRepository jwtSecurityContextRepository, LoginSuccess loginSuccess, AuthenticationFailure authenticationFailure, LogoutSuccessHandler logoutSuccessHandler) {
        this.defaultAccessDecisionManager = defaultAccessDecisionManager;
        this.jwtSecurityContextRepository = jwtSecurityContextRepository;
        this.loginSuccess = loginSuccess;
        this.authenticationFailure = authenticationFailure;
        this.logoutSuccessHandler = logoutSuccessHandler;
    }

    @Bean
    public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
        // 请求拦截处理(登录|权限 校验)
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(AUTH_WHITELIST).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().access(defaultAccessDecisionManager));
        http
                // 登录认证处理
                .securityContextRepository(jwtSecurityContextRepository)
//                .authenticationManager(reactiveAuthenticationManager())
                //Json 响应端点设置
//                .formLogin(withDefaults())
                .formLogin()
                .authenticationSuccessHandler(loginSuccess)
                .authenticationFailureHandler(authenticationFailure)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint())
                .accessDeniedHandler(new AccessDenied())
                .and().logout().logoutSuccessHandler(logoutSuccessHandler).and()
                .httpBasic(withDefaults())
                .csrf().disable();
        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters messageConverters(ObjectProvider<HttpMessageConverter<?>> converters) {
        return new HttpMessageConverters(converters.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 注册用户信息验证管理器，可按需求添加多个按顺序执行
     */
//    @Bean
//    ReactiveAuthenticationManager reactiveAuthenticationManager() {
//        LinkedList<ReactiveAuthenticationManager> managers = new LinkedList<>();
//        managers.add(authentication -> {
//            // 其他登陆方式 (比如手机号验证码登陆) 可在此设置不得抛出异常或者 Mono.error
//            return Mono.empty();
//        });
    // 必须放最后不然会优先使用用户名密码校验但是用户名密码不对时此 AuthenticationManager 会调用 Mono.error 造成后面的 AuthenticationManager 不生效
//        managers.add(new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService()));
//        return new DelegatingReactiveAuthenticationManager(managers);
//    }
}