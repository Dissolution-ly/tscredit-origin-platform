package com.tscredit.origin.oauth.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.tscredit.origin.oauth.enums.AuthEnum;
import com.tscredit.origin.oauth.oauth2.exception.TsCreditOAuth2Exception;
import com.tscredit.origin.oauth.entity.TsCreditUserDetails;
import com.tscredit.platform.base.constant.AuthConstant;
import com.tscredit.platform.base.constant.TsCreditConstant;
import com.tscredit.platform.base.domain.TsCreditUser;
import com.tscredit.platform.base.enums.ResultCodeEnum;
import com.tscredit.platform.base.result.Result;
import com.tscredit.service.system.client.feign.IUserFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.UserDeniedAuthorizationException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  实现 SpringSecurity 获取用户信息接口
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TsCreditUserDetailsServiceImpl implements UserDetailsService {

    private final IUserFeign userFeign;

    private final HttpServletRequest request;

    private final RedisTemplate redisTemplate;

    /**
     * 密码最大尝试次数
     */
    @Value("${system.maxTryTimes}")
    private int maxTryTimes;

    /**
     * 不需要验证码登录的最大尝试次数
     */
    @Value("${system.maxNonCaptchaTimes}")
    private int maxNonCaptchaTimes;

    @Override
    public TsCreditUserDetails loadUserByUsername(String username) {

        // 获取登录类型，密码，二维码，验证码
        String authGrantType = request.getParameter(AuthConstant.GRANT_TYPE);

        // 获取客户端id
        String clientId = request.getParameter(AuthConstant.AUTH_CLIENT_ID);

        // 远程调用返回数据
        Result<Object> result;

        // 通过手机号码登录
        if (!StringUtils.isEmpty(authGrantType) && AuthEnum.SMS_CAPTCHA.code.equals(authGrantType))
        {
            String phone = request.getParameter(AuthConstant.PHONE_NUMBER);
            result = userFeign.queryUserByPhone(phone);
        }
        // 通过账号密码登录
        else if(!StringUtils.isEmpty(authGrantType) && AuthEnum.QR.code.equals(authGrantType))
        {
            result = userFeign.queryUserByAccount(username);
        }
        else
        {
            result = userFeign.queryUserByAccount(username);
        }

        // 判断返回信息
        if (null != result && result.isSuccess()) {
            TsCreditUser tsCreditUser = new TsCreditUser();
            BeanUtil.copyProperties(result.getData(), tsCreditUser, false);

            // 用户名或密码错误
            if (tsCreditUser.getId() == null) {
                throw new UsernameNotFoundException(ResultCodeEnum.INVALID_USERNAME.msg);
            }

            // 没有角色
            if (CollectionUtils.isEmpty(tsCreditUser.getRoleIdList())) {
                throw new UserDeniedAuthorizationException(ResultCodeEnum.INVALID_ROLE.msg);
            }

            // 从Redis获取账号密码错误次数
            Object lockTimes = redisTemplate.boundValueOps(AuthConstant.LOCK_ACCOUNT_PREFIX + tsCreditUser.getId()).get();

            // 判断账号密码输入错误几次，如果输入错误多次，则锁定账号
            // 输入错误大于配置的次数，必须选择captcha或sms_captcha
            if (null != lockTimes && (int)lockTimes > maxNonCaptchaTimes && ( StringUtils.isEmpty(authGrantType) || (!StringUtils.isEmpty(authGrantType)
                    && !AuthEnum.SMS_CAPTCHA.code.equals(authGrantType) && !AuthEnum.CAPTCHA.code.equals(authGrantType)))) {
                throw new TsCreditOAuth2Exception(ResultCodeEnum.INVALID_PASSWORD_CAPTCHA.msg);
            }

            // 判断账号是否被锁定（账户过期，凭证过期等可在此处扩展）
            if(null != lockTimes && (int)lockTimes >= maxTryTimes){
                throw new LockedException(ResultCodeEnum.PASSWORD_TRY_MAX_ERROR.msg);
            }

            // 判断账号是否被禁用
            String userStatus = tsCreditUser.getStatus();
            if (String.valueOf(TsCreditConstant.DISABLE).equals(userStatus)) {
                throw new DisabledException(ResultCodeEnum.DISABLED_ACCOUNT.msg);
            }


            /**
             * enabled 账户是否被禁用  !String.valueOf(TsCreditConstant.DISABLE).equals(TsCreditUser.getStatus())
             * AccountNonExpired 账户是否过期  此框架暂时不提供账户过期功能，可根据业务需求在此处扩展
             * AccountNonLocked  账户是否被锁  密码尝试次数过多，则锁定账户
             * CredentialsNonExpired 凭证是否过期
             */
            return new TsCreditUserDetails(tsCreditUser.getId(), tsCreditUser.getTenantId(), tsCreditUser.getOauthId(),
                tsCreditUser.getNickname(), tsCreditUser.getRealName(), tsCreditUser.getOrganizationId(),
                tsCreditUser.getOrganizationName(),
                    tsCreditUser.getOrganizationIds(), tsCreditUser.getOrganizationNames(), tsCreditUser.getRoleId(), tsCreditUser.getRoleIds(), tsCreditUser.getRoleName(), tsCreditUser.getRoleNames(),
                tsCreditUser.getRoleIdList(), tsCreditUser.getRoleKeyList(), tsCreditUser.getResourceKeyList(),
                tsCreditUser.getDataPermissionTypeList(), tsCreditUser.getOrganizationIdList(),
                tsCreditUser.getAvatar(), tsCreditUser.getAccount(), tsCreditUser.getPassword(), !String.valueOf(TsCreditConstant.DISABLE).equals(tsCreditUser.getStatus()), true, true, true,
                this.getPrivileges(tsCreditUser.getRoleKeyList(), tsCreditUser.getResourceUrlList()));
        } else {
            throw new UsernameNotFoundException(result.getMsg());
        }
    }

    /**
     * 设置SpringSecurity需要的role和resource
     * @param roles
     * @param resources
     * @return
     */
    private final List<GrantedAuthority> getPrivileges(final Collection<String> roles, final Collection<String> resources) {
        final List<GrantedAuthority> authorities = new ArrayList<>();

        for (final String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }

        //不将resource权限加入token，这样会导致请求头很大
//        for (final String resource : resources) {
//            authorities.add(new SimpleGrantedAuthority(resource));
//        }

        return authorities;
    }

}
