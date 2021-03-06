package com.tscredit.origin.oauth.action;

import cn.hutool.core.bean.BeanUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.tscredit.origin.oauth.utils.CaptchaUtils;
import com.tscredit.origin.oauth.utils.JwtUtils;
import com.tscredit.platform.base.annotation.auth.CurrentUser;
import com.tscredit.platform.base.constant.AuthConstant;
import com.tscredit.platform.base.constant.TokenConstant;
import com.tscredit.platform.base.constant.TsCreditConstant;
import com.tscredit.platform.base.domain.TsCreditUser;
import com.tscredit.platform.base.exception.BusinessException;
import com.tscredit.platform.base.result.Result;
import com.tscredit.platform.captcha.constant.CaptchaConstant;
import com.tscredit.platform.captcha.domain.ImageCaptcha;
import com.tscredit.platform.oauth2.domain.Oauth2Token;
import com.tscredit.service.extension.client.feign.ISmsFeign;
import com.tscredit.service.system.client.feign.IUserFeign;
import com.wf.captcha.SpecCaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Api(tags = "OAuth2????????????")
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/oauth")
public class OAuthController {

    private final TokenEndpoint tokenEndpoint;

    private final RedisTemplate redisTemplate;

    private final KeyPair keyPair;

    private final CaptchaService captchaService;

    private final IUserFeign userFeign;

    private final ISmsFeign smsFeign;

    @Value("${captcha.type}")
    private String captchaType;

    @ApiOperation("????????????????????????????????????")
    @GetMapping("/captcha/type")
    public Result captchaType() {
        return Result.data(captchaType);
    }

    @ApiOperation("?????????????????????")
    @PostMapping("/captcha")
    public Result captcha(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.get(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("?????????????????????")
    @PostMapping("/captcha/check")
    public Result captchaCheck(@RequestBody CaptchaVO captchaVO) {
        ResponseModel responseModel = captchaService.check(captchaVO);
        return Result.data(responseModel);
    }

    @ApiOperation("?????????????????????")
    @RequestMapping("/captcha/image")
    public Result captchaImage() {
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        String captchaCode = specCaptcha.text().toLowerCase();
        String captchaKey = UUID.randomUUID().toString();
        // ??????redis????????????????????????5??????
        redisTemplate.opsForValue().set(CaptchaConstant.IMAGE_CAPTCHA_KEY + captchaKey, captchaCode, TsCreditConstant.Number.FIVE,
                TimeUnit.MINUTES);
        ImageCaptcha imageCaptcha = new ImageCaptcha();
        imageCaptcha.setCaptchaKey(captchaKey);
        imageCaptcha.setCaptchaImage(specCaptcha.toBase64());
        // ???key???base64???????????????
        return Result.data(imageCaptcha);
    }

    @ApiOperation("OAuth2??????token")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", value = "????????????", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "password"),
            @ApiImplicitParam(name = "client_id", value = "Oauth2?????????ID", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "client"),
            @ApiImplicitParam(name = "client_secret", value = "Oauth2???????????????", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "123456"),
            @ApiImplicitParam(name = "refresh_token", value = "??????token", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "username", value = "???????????????", dataType = "string", dataTypeClass = String.class, defaultValue = "admin"),
            @ApiImplicitParam(name = "password", value = "????????????", dataType = "string", dataTypeClass = String.class, defaultValue = "123456"),
            @ApiImplicitParam(name = "phoneNumber", value = "????????????", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "smsCode", value = "????????????code", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "?????????code/???????????????", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "encryptedData", value = "????????????????????????????????????????????????????????????", dataType = "string", dataTypeClass = String.class),
            @ApiImplicitParam(name = "iv", value = "???????????????????????????", dataType = "string", dataTypeClass = String.class),
    })
    @PostMapping("/token")
    public Result postAccessToken(@ApiIgnore Principal principal, @ApiIgnore @RequestParam Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        //??????????????????????????????account???md5????????????
        String username = parameters.get("username");
        String password = parameters.get("password");
        Result<Object> result = userFeign.queryUserByAccount(username);
        if (null != result && result.isSuccess()) {
            TsCreditUser tsCreditUser = new TsCreditUser();
            BeanUtil.copyProperties(result.getData(), tsCreditUser, false);
            if (!StringUtils.isEmpty(tsCreditUser.getAccount())) {
                username = tsCreditUser.getAccount();
                password = AuthConstant.BCRYPT + tsCreditUser.getAccount() + password;
                parameters.put("username", username);
                parameters.put("password", password);
            }
        }

        OAuth2AccessToken oAuth2AccessToken = tokenEndpoint.postAccessToken(principal, parameters).getBody();
        DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) oAuth2AccessToken.getRefreshToken();
        Oauth2Token oauth2Token = Oauth2Token.builder()
                .token(oAuth2AccessToken.getValue())
                .expiresIn(oAuth2AccessToken.getExpiresIn())
                .exp(oAuth2AccessToken.getExpiration().getTime() / TsCreditConstant.Number.THOUSAND)
                .refreshToken(refreshToken.getValue())
                .refreshExpiresIn((int) (refreshToken.getExpiration().getTime() / TsCreditConstant.Number.THOUSAND - System.currentTimeMillis() / TsCreditConstant.Number.THOUSAND))
                .refreshExp(refreshToken.getExpiration().getTime() / TsCreditConstant.Number.THOUSAND)
                .tokenHead(AuthConstant.JWT_TOKEN_PREFIX)
                .build();
        return Result.data(oauth2Token);
    }

    @ApiOperation("?????????????????????")
    @PostMapping("/sms/captcha/send")
    public Result sendSmsCaptcha(@ApiIgnore @RequestParam Map<String, String> parameters) {
        boolean checkCaptchaResult = CaptchaUtils.checkCaptcha(parameters, redisTemplate, captchaService);
        Result<Object> sendResult;
        if (checkCaptchaResult) {
            sendResult = smsFeign.sendSmsVerificationCode(parameters.get(TokenConstant.SMS_CODE), parameters.get(TokenConstant.PHONE_NUMBER));
        } else {
            throw new BusinessException("?????????????????????????????????????????????????????????");
        }
        return sendResult;
    }

    /**
     * ????????????????????????????????????????????????
     * 1?????????????????????????????????????????????????????????????????????token??????????????????????????????token???????????????????????????????????????????????????????????????token???????????????redis??????
     * 2???????????????????????????????????????????????????????????????token??????????????????refresh_token????????????????????????????????????
     * ??????????????????????????????????????????????????????
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {

        String token = request.getHeader(AuthConstant.JWT_TOKEN_HEADER);
        String refreshToken = request.getParameter(AuthConstant.REFRESH_TOKEN);
        long currentTimeSeconds = System.currentTimeMillis() / TsCreditConstant.Number.THOUSAND;

        // ???token???refresh_token?????????????????????
        String[] tokenArray = new String[TsCreditConstant.Number.TWO];
        tokenArray[TsCreditConstant.Number.ZERO] = token.replace(AuthConstant.JWT_TOKEN_PREFIX, "");
        tokenArray[TsCreditConstant.Number.ONE] = refreshToken;
        for (int i = TsCreditConstant.Number.ZERO; i < tokenArray.length; i++) {
            String realToken = tokenArray[i];
            Map<String, Object> jsonObject = JwtUtils.decodeJwt(realToken);
            String jti = jsonObject.getOrDefault(TokenConstant.JTI, null).toString();
            Long exp = Long.parseLong(jsonObject.getOrDefault(TokenConstant.EXP, null).toString());
            if (exp - currentTimeSeconds > TsCreditConstant.Number.ZERO) {
                redisTemplate.opsForValue().set(AuthConstant.TOKEN_BLACKLIST + jti, jti, (exp - currentTimeSeconds), TimeUnit.SECONDS);
            }
        }
        return Result.success();
    }

    @GetMapping("/user/info")
    public Result<TsCreditUser> currentUser(@ApiIgnore @CurrentUser TsCreditUser user) {
        return Result.data(user);
    }

    @GetMapping("/public_key")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
