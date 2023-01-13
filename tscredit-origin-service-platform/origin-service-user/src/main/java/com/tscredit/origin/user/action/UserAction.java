package com.tscredit.origin.user.action;


import com.aurora.base.constant.ErrorMessage;
import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.exception.LogicException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.User;
import com.tscredit.origin.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Tag(name ="用户管理", description = "UserAction")
@Slf4j
@RestController
@RequestMapping("/user")
public class UserAction {

    private final UserService userService;

    public UserAction(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "分页列表查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "页数", required = true),
            @Parameter(name = "pageSize", description = "每页条数", required = true)
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, User user) {
        return ActionMessage.success().data(userService.pageList(new Page<>(pageNum, pageSize), user));
    }

    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(userService.getById(id));
    }

    @Operation(summary = "保存/修改")
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(@Validated(User.Insert.class) @RequestBody User user) {
        // 验证用户名称是否重复
        if (userService.getUserByName(user.getLoginName(), user.getId() + "") != null) {
            throw LogicException.errorMessage(ErrorMessage.USER_REPEAT);
        }

        // 新增(userId为空) / 修改  的额外设置
        if (StringUtils.isEmpty(user.getId())) {
            // TODO 密码加密
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
            // 设置地区类型默认为地区
            user.setDistrictType("1");
        } else {
            // 忽略密码设置
            user.setPassword(null);
        }

        return ActionMessage.success().data(userService.saveOrUpdate(user));
    }

    @Operation(summary = "删除")
    @Parameters({
            @Parameter(name = "ids", description = "id集合(字符串数组)", required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<String> ids) {
        return ActionMessage.success().data(userService.removeByIds(ids));
    }

    @Operation(summary = "初始化密码")
    @PostMapping(value = "/initPassword.do")
    public ActionMessage chuPassword(@RequestParam(value = "id") String userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        String password = user.getLoginName() + "@" + new SimpleDateFormat("MMdd").format(new Date());
        // TODO 密码加密
//        user.setPassword(passwordEncoder.encode(password));
        log.info("用户" + user.getLoginName() + "密码初始化为： " + password);
        return ActionMessage.success().data(userService.updateById(user));
    }

    @Operation(summary = "验证账号是否注册过")
    @PostMapping(value = "/isRegister")
    public ActionMessage isRegister(@RequestParam(value = "number") String number, String id) {
        return ActionMessage.success().data(userService.getUserByName(number, id) != null);
    }
}
