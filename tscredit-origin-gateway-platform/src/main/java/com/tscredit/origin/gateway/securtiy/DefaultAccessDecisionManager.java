package com.tscredit.origin.gateway.securtiy;

import com.aurora.redis.config.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthenticatedReactiveAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;


@Slf4j
@Component
@RefreshScope
public class DefaultAccessDecisionManager implements ReactiveAuthorizationManager<AuthorizationContext> {


    @Value("${user.check.authority:true}")
    private boolean enableAuthorityCheck;

    @Value("${user.check.login:true}")
    private boolean enableLoginCheck;

    private final RedisUtil redisUtil;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    public DefaultAccessDecisionManager(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {

        Mono<Boolean> result = Mono.just(true);

        // 登录校验
        if (enableLoginCheck) {
//            AuthenticatedReactiveAuthorizationManager.authenticated().check(authentication, authorizationContext);
            result = authentication.filter(auth -> !new AuthenticationTrustResolverImpl().isAnonymous(auth))
                    .map(Authentication::isAuthenticated)
                    .defaultIfEmpty(false);
        }

        // 权限校验
        if (enableAuthorityCheck) {
            result = authentication.map(auth -> {
                ServerHttpRequest request = authorizationContext.getExchange().getRequest();
                String path = request.getURI().getPath();
                for (GrantedAuthority authority : auth.getAuthorities()) {
                    // redis 需要保证对应角色的黑名单一定存在，即使黑名单是空的 (因为未初始化角色数据和新建角色是空的，要做区分)
                    String urls = (String) redisUtil.hget("authority:role_url", authority.getAuthority());
                    if (urls != null) {
                        // 查询用户访问所需角色进行对比
                        boolean isGranted = true;
                        for (String url : urls.split(",")) {
                            if (antPathMatcher.match(url, path)) {
                                isGranted = false;
                                break;
                            }
                        }
                        if (isGranted) {
                            log.info(String.format("角色URL校验通过，GrantedAuthority:{%s}  Path:{%s} ", authority.getAuthority(), path));
                            return true;
                        }
                    }
                }
                return false;
            }).defaultIfEmpty(false);
        }

        return result.map(AuthorizationDecision::new);
//        return AuthenticatedReactiveAuthorizationManager.authenticated().check(authentication, authorizationContext);
    }


    @Override
    public Mono<Void> verify(Mono<Authentication> authentication, AuthorizationContext authorizationContext) {
        return ReactiveAuthorizationManager.super.verify(authentication, authorizationContext);
    }

}
