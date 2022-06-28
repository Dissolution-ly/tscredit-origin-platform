package com.tscredit.origin.oauth.oauth2.granter;

import com.anji.captcha.service.CaptchaService;
import com.tscredit.service.extension.client.feign.ISmsFeign;
import com.tscredit.service.system.client.feign.IUserFeign;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自定义token
 */
public class TsCreditTokenGranter {

    /**
     * 自定义tokenGranter
     */
    public static TokenGranter getTokenGranter(final AuthenticationManager authenticationManager,
                                               final AuthorizationServerEndpointsConfigurer endpoints, RedisTemplate redisTemplate, IUserFeign userFeign,
                                               ISmsFeign smsFeign, CaptchaService captchaService, UserDetailsService userDetailsService, String captchaType) {
        // 默认tokenGranter集合
        List<TokenGranter> granters = new ArrayList<>(Collections.singletonList(endpoints.getTokenGranter()));
        // 增加验证码模式
        granters.add(new CaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
            endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, captchaService,
            captchaType));
        // 增加短信验证码模式
        granters.add(new SmsCaptchaTokenGranter(authenticationManager, endpoints.getTokenServices(),
                endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory(), redisTemplate, userFeign, smsFeign, captchaService,
                userDetailsService, captchaType));
        // 组合tokenGranter集合
        return new CompositeTokenGranter(granters);
    }

}
