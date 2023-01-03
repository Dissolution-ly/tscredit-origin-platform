package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tscredit.origin.user.entity.dao.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "User对象", description = "")
public class UserInfo extends User {

    @Schema(name = "重复输入密码,修改密码时验证")
    @TableField(exist = false)
    private String rePassword;

    @Schema(name = "旧密码")
    @TableField(exist = false)
    private String oldPassword;

    @Schema(name = "用户角色ID")
    private String roleIds;

    @Schema(name = "角色id列表")
    private List<String> roleIdList;

    @Schema(name = "用户角色CODE")
    private String roleKeys;

    @Schema(name = "角色key列表")
    private List<String> roleKeyList;

    @Schema(name = "用户角色")
    private String roleName;
}
