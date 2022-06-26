package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tscredit.origin.user.entity.dao.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "User对象", description = "")
public class UserInfo extends User {

    @ApiModelProperty(value = "重复输入密码,修改密码时验证")
    @TableField(exist = false)
    private String rePassword;

    @ApiModelProperty(value = "旧密码")
    @TableField(exist = false)
    private String oldPassword;

    @ApiModelProperty(value = "用户角色ID")
    private String roleIds;

    @ApiModelProperty(value = "角色id列表")
    private List<String> roleIdList;

    @ApiModelProperty(value = "用户角色CODE")
    private String roleKeys;

    @ApiModelProperty(value = "角色key列表")
    private List<String> roleKeyList;

    @ApiModelProperty(value = "用户角色")
    private String roleName;
}
