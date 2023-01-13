package com.tscredit.origin.user.action;


import com.aurora.base.entity.response.ActionMessage;
import com.aurora.boot.validataed.Flag;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tscredit.origin.user.entity.User;
import com.tscredit.origin.user.service.RoleMenuService;
import com.tscredit.origin.user.service.UserService;
import com.tscredit.origin.user.utils.IPUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Slf4j
@RestController
@Tag(name ="用户管理", description = "LoginAction")
public class LoginAction {

    private final RoleMenuService roleMenuService;
    private final UserService userService;

    public LoginAction(RoleMenuService roleMenuService, UserService userService) {
        this.roleMenuService = roleMenuService;
        this.userService = userService;
    }


    @Operation(summary = "登录")
    @PostMapping(value = "/login")
    public void changePassword(@RequestParam String username, @RequestParam String password) {
        System.out.println(username + password);
    }

    @Operation(summary = "修改密码")
    @PostMapping(value = "/userInfo/changePassword")
    public ActionMessage changePassword(User user) {
        // 数据库查出当前用户密码
        User oldUser = userService.getById(user.getId());

        // TODO 与输入原密码做比对
//        if (!passwordEncoder.matches(user.getOldPassword(), oldUser.getPassword())) {
//            return ActionMessage.error().msg("原密码填写错误");
//        }
//
//        //  TODO 设置新密码
//        oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return ActionMessage.success().data(userService.updateById(oldUser));
    }

    @Operation(summary = "查询当前登录用户的角色 对应的 菜单/权限")
    @PostMapping(value = "/userInfo/selectRoleMenu")
    public ActionMessage selectRoleMenu(HttpServletRequest request) {
        // 此处需要获取登录的用户
//        Map<String, Object> user = IPUtil.token(request);
//        Map<Object, Object> result = new HashMap<>(16);
//        result.put("roleMenu", roleMenuService.selectRoleMenu(MapUtils.getString(user, "roleCode"), "url"));
//        result.put("roleMenuList", roleMenuService.selectRoleMenu(MapUtils.getString(user, "roleCode")));
//        return ActionMessage.success().data(result);
        return null;
    }

    @Operation(summary = "禁用用戶")
    @Parameters({
            @Parameter(name = "ids", description = "id集合(字符串数组)", required = true),
    })
    @PostMapping("/disable")
    public ActionMessage disable(@RequestParam List<String> ids, @Flag(values = {"0", "1"}) @RequestParam Integer userStatus) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.in("fu_id", ids);
        wrapper.eq("fu_status", "1");

        User user = new User();
        user.setUserstatus(userStatus.toString());
        return ActionMessage.success().data(userService.update(user, wrapper));
    }

}
