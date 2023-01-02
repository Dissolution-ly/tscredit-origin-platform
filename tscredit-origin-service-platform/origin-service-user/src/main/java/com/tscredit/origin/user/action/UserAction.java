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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.util.List;


@Api(tags = {"用户管理"}, value = "UserAction")
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

    @ApiOperation("分页列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", dataType = "int", dataTypeClass = Integer.class, required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", dataTypeClass = Integer.class, required = true, defaultValue = "20")
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, QueryUserDTO user) {
        return ActionMessage.success().data(userService.pageList(new Page<>(pageNum, pageSize), user));
    }

    @ApiOperation("保存用户")
    @PostMapping("/saveOrUpdate")
    public ActionMessage saveOrUpdate(@RequestBody CreateUserDTO user) {
        if (!userService.createUser(user)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();

    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户")
    public ActionMessage update(@RequestBody UpdateUserDTO updateUserDTO) {
        if (!userService.updateUser(updateUserDTO)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    @ApiOperation("批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合(字符串数组)", dataType = "long", dataTypeClass = Long.class, required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<Long> ids) {
        if (!userService.batchDeleteUser(ids)) {
            return ActionMessage.error(ErrorMessage.SYS_REQUEST_ERROR);
        }
        return ActionMessage.success();
    }

    @ApiOperation("自己-修改密码")
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

    @ApiOperation("管理员-初始化|重置密码")
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
    @ApiOperation(value = "管理员修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", dataType = "long", dataTypeClass = Long.class, paramType = "path", required = true),
            @ApiImplicitParam(name = "status", value = "用户状态", dataType = "string", dataTypeClass = String.class, paramType = "path", required = true)
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
    @ApiOperation(value = "用户修改个人信息")
    public Result<?> updateInfo(@RequestBody UpdateUserDTO user, @ApiIgnore User tempUser) {
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

    @ApiOperation("根据Id获取基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "string", defaultValue = "1"),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(userService.getById(id));
    }


    @ApiOperation(value = "校验用户账号是否存在", notes = "校验用户账号是否存在")
    @PostMapping(value = "/check")
    public ActionMessage checkUserExist(@Validated @NotNull QueryUserDTO queryUserDTO) {
        User user = BeanCopierUtils.copyByClass(queryUserDTO, User.class);
        boolean isExist = userService.getUserByLoginInfo(user) != null;
        return ActionMessage.success().data(!isExist);
    }

}
