package com.tscredit.origin.user.action.feign;

import com.aurora.base.entity.response.Result;
import com.tscredit.origin.user.entity.UserInfo;
import com.tscredit.origin.user.entity.dto.QueryUserDTO;
import com.tscredit.origin.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserFeign
 * @Description: UserFeign前端控制器
 * @date 2019年5月18日 下午4:03:58
 */
@RestController
@RequestMapping(value = "/feign/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Tag(name = "UserFeign|提供微服务调用接口")
@RefreshScope
public class UserFeign {

    @Value("${server.port}")
    private Integer serverPort;

    private final UserService userService;

    @GetMapping(value = "/query/by/phone")
    @Operation(summary = "通过手机号码查询用户信息", description = "通过手机号码查询用户信息")
    public Result<UserInfo> queryByPhone(String phoneNumber) {
        QueryUserDTO user = new QueryUserDTO();
        user.setMobile(phoneNumber);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.success(userInfo);
    }

    @GetMapping(value = "/query/by/account")
    @Operation(summary = "通过账号查询用户信息", description = "通过账号查询用户信息")
    public Result<UserInfo> queryByAccount(String loginName) {
        QueryUserDTO user = new QueryUserDTO();
        user.setLoginName(loginName);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.success(userInfo);
    }

    @GetMapping(value = "/query/by/openid")
    @Operation(summary = "通过微信openid查询用户信息", description = "通过微信openid查询用户信息")
    public Result<UserInfo> queryByOpenId(String openid) {
        QueryUserDTO user = new QueryUserDTO();
        user.setMobile("18888888888");
        // TODO 此处待定
//        UserInfo userInfo = userService.queryUserInfo(user);
        UserInfo userInfo = new UserInfo();
        userInfo.setMobile(serverPort + "");
        return Result.success(userInfo);
    }
}
