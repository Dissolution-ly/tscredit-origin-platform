package com.tscredit.origin.user.action;


import com.aurora.base.constant.ErrorMessage;
import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.entity.response.Result;
import com.aurora.base.exception.LogicException;
import com.aurora.boot.util.BeanCopierUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.dao.User;
import com.tscredit.origin.user.entity.dto.CreateUserDTO;
import com.tscredit.origin.user.entity.dto.QueryUserDTO;
import com.tscredit.origin.user.entity.dto.UpdateUserDTO;
import com.tscredit.origin.user.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@Tag(name = "用户管理", description = "UserAction")
@Slf4j
@Validated
@RefreshScope
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserAction {

    @Value("${user.defaultPwd}")
    private String defaultPwd;
    private final UserService userService;

    @Operation(summary = "分页列表查询")
    @Parameters({
            @Parameter(name = "pageNum", description = "页数", required = true),
            @Parameter(name = "pageSize", description = "每页条数", required = true)
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, QueryUserDTO user) {
        return ActionMessage.success().data(userService.pageList(new Page<>(pageNum, pageSize), user));
    }

    @Operation(summary = "保存用户")
    @PostMapping("/saveOrUpdate")
    public ActionMessage saveOrUpdate(@RequestBody CreateUserDTO user) {
        if (!userService.createUser(user)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();

    }

    @PostMapping("/update")
    @Operation(summary = "修改用户")
    public ActionMessage update(@RequestBody UpdateUserDTO updateUserDTO) {
        if (!userService.updateUser(updateUserDTO)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    @Operation(summary = "批量删除")
    @Parameters({
            @Parameter(name = "ids", description = "id集合(字符串数组)", required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<Long> ids) {
        if (!userService.batchDeleteUser(ids)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    @Operation(summary = "自己-修改密码")
    @PostMapping(value = "/changePassword")
    public ActionMessage changePassword(UpdateUserDTO updateUserDTO) {
        String newPwd = updateUserDTO.getNewPwd();
        String oldPwd = updateUserDTO.getOldPwd();
        if (StringUtils.isEmpty(newPwd) || StringUtils.isEmpty(oldPwd)) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        return null;
        // TODO 对比原密码
//        if (tempUser == null || !BCrypt.checkpw(tempUser.getAccount() + oldPwd, tempUser.getPassword())) {
//            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
//        }
//        UpdateUserDTO user = new UpdateUserDTO();
//        user.setId(tempUser.getId());
//        user.setPassword(newPwd);
//        boolean result = userService.updateUser(user);

    }

    @Operation(summary = "管理员-初始化|重置密码")
    @PostMapping(value = "/initPassword")
    public ActionMessage initPassword(@RequestParam(value = "id") Long userId) {
        // 验证是否存在
        User user = userService.getById(userId);
        if (user == null) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        // 设置密码
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(userId);
        updateUserDTO.setPassword(defaultPwd);
        if (!userService.updateUser(updateUserDTO)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    /**
     * 修改用户状态
     */
    @PostMapping("/status/{userId}/{status}")
    @Operation(summary = "管理员修改用户状态")
    @Parameters({
            @Parameter(name = "userId", description = "用户ID",  required = true),
            @Parameter(name = "status", description = "用户状态", required = true)
    })
    public ActionMessage updateStatus(@PathVariable("userId") @NotNull Long userId, @PathVariable("status") @NotNull String status) {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setId(userId);
        updateUserDTO.setUserStatus(status);
        if (!userService.updateUser(updateUserDTO)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    /**
     * 个人修改用户信息，限制修改字段
     */
    @PostMapping("/update/info")
    @Operation(summary = "用户修改个人信息")
    public Result<?> updateInfo(@RequestBody UpdateUserDTO user, @Parameter(hidden = true) User tempUser) {
//        UpdateUserDTO upUser = new UpdateUserDTO();
//        upUser.setAvatar(user.getAvatar());
//        upUser.setRealName(user.getRealName());
//        upUser.setNickname(user.getNickname());
//        upUser.setAreas(user.getAreas());
//        upUser.setId(tempUser.getId());
//        boolean result = userService.updateUser(user);
//        if (result) {
//            return Result.success();
//        } else {
//            return Result.error(ResultCodeEnum.FAILED);
//        }
        return null;
    }

    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
            @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(userService.getById(id));
    }


    @Operation(summary = "校验用户账号是否存在", description = "校验用户账号是否存在")
    @PostMapping(value = "/check")
    public ActionMessage checkUserExist(@Validated @NotNull QueryUserDTO queryUserDTO) {
        User user = BeanCopierUtils.copyByClass(queryUserDTO, User.class);
        boolean isExist = userService.getUserByLoginInfo(user) != null;
        return ActionMessage.success().data(!isExist);
    }

}
