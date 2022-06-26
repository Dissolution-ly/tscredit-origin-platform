package com.tscredit.origin.user.action;


import com.tscredit.common.response.ActionMessage;
import com.tscredit.origin.user.entity.dao.User;
import com.tscredit.origin.user.service.RoleResourceService;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.origin.user.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@Api(tags = {"用户管理"}, value = "LoginAction")
public class LoginAction {

    private final RoleResourceService roleResourceService;
    private final UserService userService;

    public LoginAction(RoleResourceService roleResourceService, UserService userService) {
        this.roleResourceService = roleResourceService;
        this.userService = userService;
    }


    @ApiOperation("登录")
    @PostMapping(value = "/login")
    public void changePassword(@RequestParam String username, @RequestParam String password) {
        System.out.println(username + password);
    }

    /**
     * 根据token获取用户信息
     */
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    @PostMapping(value = "/userInfo/getByToken")
    public ActionMessage getUserByToken(HttpServletRequest request) {
        return ActionMessage.success().data(JwtUtil.token(request));
    }

    @ApiOperation("修改密码")
    @PostMapping(value = "/userInfo/changePassword")
    public ActionMessage changePassword(User user) {
/*        // TODO 数据库查出当前用户密码
        User oldUser = JwtUtil.getLoginUser();
        // 与输入原密码做比对
        if (!passwordEncoder.matches(user.getOldPassword(), oldUser.getPassword())) {
            return ActionMessage.error().msg("原密码填写错误");
        }

        // 设置新密码
        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return ActionMessage.success().data(userService.updateById(oldUser));*/
        return null;
    }

    @ApiOperation("查询当前登录用户的角色 对应的 菜单/权限")
    @PostMapping(value = "/userInfo/selectRoleMenu")
    public ActionMessage selectRoleMenu(HttpServletRequest request) {
        Map<String, Object> user = JwtUtil.token(request);
        Map<Object, Object> result = new HashMap<>(16);
        result.put("roleMenu", roleResourceService.selectRoleMenu(MapUtils.getString(user, "roleCode"), "url"));
        result.put("roleMenuList", roleResourceService.selectRoleMenu(MapUtils.getString(user, "roleCode")));
        return ActionMessage.success().data(result);
    }
}
