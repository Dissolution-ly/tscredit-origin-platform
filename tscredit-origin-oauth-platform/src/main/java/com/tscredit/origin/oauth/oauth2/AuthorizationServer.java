package com.tscredit.origin.oauth.oauth2;

import com.tscredit.origin.oauth.oauth2.exception.TsCreditOAuth2ExceptionTranslator;
import com.tscredit.origin.oauth.service.impl.TsCreditClientDetailsServiceImpl;
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
 * `???????????????` ??????
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {

    private final DataSource dataSource;

    // ???????????? Spring Security ??????
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
        // ????????????????????????????????????
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
     * Token ????????????
     */
    @Bean
    AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        services.setClientDetailsService(clientDetailsService());
        // Token ??????????????????
        services.setSupportRefreshToken(true);
        //??????????????????RefreshToken
        services.setReuseRefreshToken(false);
        // Token ????????????
        services.setTokenStore(tokenStore);
        // Token ?????????
        services.setAccessTokenValiditySeconds(60 * 60 * 2);
        // refresh_token ????????????
        services.setRefreshTokenValiditySeconds(60 * 60 * 24 * 3);

        // ??????JWT????????????????????? ??? DefaultTokenServices ????????? TokenEnhancer
        // ??? JwtAccessTokenConverter???CustomAdditionalInformation ???????????? TokenEnhancer
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter, myTokenEnhancer));
        services.setTokenEnhancer(tokenEnhancerChain);
        return services;
    }


    /**
     * ????????????????????????: `?????????`???`??????`??? ??????????????????????????????
     * <p>
     * AuthorizationServerEndpointsConfigurer ?????????????????????????????????????????????(Grant Types):
     * authorizationCodeServices: ??????????????????????????????
     * authenticationManager(Security???????????????)??????????????????????????????
     * userDetailsService????????????UserDetailsService??????
     * implicitGrantService??????????????????????????????
     * tokenGranter
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        // ????????? TokenGranter ????????? oauth2 ??????
//        TokenGranter tokenGranter = TsCreditTokenGranter.getTokenGranter(authenticationManager, endpoints, redisTemplate,
//                userFeign, smsFeign, captchaService, userDetailsService, captchaType);

        endpoints
                // ??????????????????????????? authenticationManager
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                // ??????????????????????????????`?????????`?????????
                .authorizationCodeServices(authorizationCodeServices())
                // `??????` ???????????????????????????
                .tokenServices(tokenServices())
                // ???????????????????????????
                .allowedTokenEndpointRequestMethods(HttpMethod.POST)
                // ?????? oauth2 ??????
//                .tokenGranter(tokenGranter)
                /*
                 * refresh_token????????????????????????????????????(true)??????????????????(false)????????????true
                 *      1.???????????????access_token?????????????????? refresh token?????????????????????????????????????????????????????????
                 *      2.??????????????????access_token?????????????????? refresh_token??????????????????
                 */
                .reuseRefreshTokens(false)
                // ?????????????????????
                .exceptionTranslator(tsCreditOAuth2ExceptionTranslator);
    }

    /**
     * ?????? ???????????? ??? ???????????? ???????????????(??????????????????????????????)
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                // ??????????????????
                .allowFormAuthenticationForClients();
    }

    /**
     * ?????? `?????????` ????????????
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService());
    }
}
