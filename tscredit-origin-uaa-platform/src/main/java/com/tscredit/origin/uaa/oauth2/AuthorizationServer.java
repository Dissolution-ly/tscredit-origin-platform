package com.tscredit.origin.uaa.oauth2;

import com.tscredit.origin.uaa.oauth2.exception.TsCreditOAuth2ExceptionTranslator;
import com.tscredit.origin.uaa.service.impl.TsCreditClientDetailsServiceImpl;
import com.tscredit.service.extension.client.feign.ISmsFeign;
import com.tscredit.service.system.client.feign.IUserFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.Arrays;


/**
 * `授权服务器` 配置
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    // 该属性由 Spring Security 提供
    private final AuthenticationManager authenticationManager;

    private final TokenStore tokenStore;

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    private final MyTokenEnhancer myTokenEnhancer;

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final TsCreditOAuth2ExceptionTranslator tsCreditOAuth2ExceptionTranslator;

    private final IUserFeign userFeign;

    private final ISmsFeign smsFeign;


    @Bean
    ClientDetailsService clientDetailsService() {
        TsCreditClientDetailsServiceImpl jdbcClientDetailsService = new TsCreditClientDetailsServiceImpl(dataSource);
        // 为客户端查询增加逻辑删除
//        jdbcClientDetailsService.setFindClientDetailsSql(AuthConstant.FIND_CLIENT_DETAILS_SQL);
//        jdbcClientDetailsService.setSelectClientDetailsSql(AuthConstant.SELECT_CLIENT_DETAILS_SQL);
        jdbcClientDetailsService.setPasswordEncoder(passwordEncoder);
        return jdbcClientDetailsService;
    }

    @Bean
    AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    /**
     * Token 基本信息
     */
    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService());
        // Token 是否支持刷新
        services.setSupportRefreshToken(true);
        //是否重复使用RefreshToken
        services.setReuseRefreshToken(false);
        // Token 存储位置
        services.setTokenStore(tokenStore);
        // Token 有效期
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        // refresh_token 的有效期
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);

        // 使用JWT存储方式时需要 在 DefaultTokenServices 中配置 TokenEnhancer
        // 将 JwtAccessTokenConverter、CustomAdditionalInformation 实例注入 TokenEnhancer
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter, myTokenEnhancer));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }


    /**
     * 令牌访问端点配置: `授权码`和`令牌`的 生成方式、存储位置、
     * <p>
     * AuthorizationServerEndpointsConfigurer 通过以下属性决定支持的授权类型(Grant Types):
     * authorizationCodeServices: 使用授权码模式时需要
     * authenticationManager(Security的认证管理)：使用密码模式时需要
     * userDetailsService：自定义UserDetailsService实现
     * implicitGrantService：使用简单模式时需要
     * tokenGranter
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // 自定义 TokenGranter 以增加 oauth2 模式
//        TokenGranter tokenGranter = TsCreditTokenGranter.getTokenGranter(authenticationManager, endpoints, redisTemplate,
//                userFeign, smsFeign, captchaService, userDetailsService, captchaType);

        endpoints
                // 密码模式需要：配置 authenticationManager
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                // 授权码模式需要：配置`授权码`的存储
                .authorizationCodeServices(authorizationCodeServices())
                // `令牌` 生成方式、存储位置
                .tokenServices(tokenServices())
                // 端点允许的请求方法
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                // 增加 oauth2 模式
//                .tokenGranter(tokenGranter)
                /*
                 * refresh_token有两种使用方式：重复使用(true)、非重复使用(false)，默认为true
                 *      1.重复使用：access_token过期刷新时， refresh token过期时间未改变，仍以初次生成的时间为准
                 *      2.非重复使用：access_token过期刷新时， refresh_token过期时间延续
                 */
                .reuseRefreshTokens(false)
                // 自定义异常信息
                .exceptionTranslator(tsCreditOAuth2ExceptionTranslator);
    }

    /**
     * 配置 令牌端点 和 令牌服务 的安全约束(哪些人能访问哪些端点)
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                // 允许表单验证
                .allowFormAuthenticationForClients();
    }

    /**
     * 配置 `客户端` 存储位置
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }
}
