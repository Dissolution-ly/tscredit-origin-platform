package com.tscredit.origin.user.action.feign;

import com.tscredit.origin.user.entity.UserInfo;
import com.tscredit.origin.user.entity.dto.QueryUserDTO;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.platform.base.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "UserFeign|提供微服务调用接口")
@RefreshScope
public class UserFeign {

    private final UserService userService;

    @GetMapping(value = "/query/by/phone")
    @ApiOperation(value = "通过手机号码查询用户信息", notes = "通过手机号码查询用户信息")
    public Result<UserInfo> queryByPhone(String phoneNumber) {
        QueryUserDTO user = new QueryUserDTO();
        user.setMobile(phoneNumber);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    @GetMapping(value = "/query/by/account")
    @ApiOperation(value = "通过账号查询用户信息", notes = "通过账号查询用户信息")
    public Result<UserInfo> queryByAccount(String loginName) {
        QueryUserDTO user = new QueryUserDTO();
        user.setLoginName(loginName);
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }

    @GetMapping(value = "/query/by/openid")
    @ApiOperation(value = "通过微信openid查询用户信息", notes = "通过微信openid查询用户信息")
    public Result<UserInfo> queryByOpenId(String openid) {
        QueryUserDTO user = new QueryUserDTO();
        // TODO 此处待定
        UserInfo userInfo = userService.queryUserInfo(user);
        return Result.data(userInfo);
    }
}
