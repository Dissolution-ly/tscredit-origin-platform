package com.tscredit.service.client.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

@Data
@ToString
//@RePassWord(values = {"password", "rePassword"}, groups = {User.UpdatePassword.class})
@Schema(name  = "User对象", description = "")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name  = "用户id")
    private Integer id;

    @Schema(name  = "用户名称(昵称)")
    private String userName;

    @Schema(name  = "登录账号")
    private String loginName;

    @Schema(name  = "登录密码")
    private String password;

    @Schema(name  = "重复输入密码,修改密码时验证")
    private String rePassword;

    @Schema(name  = "旧密码")
    private String oldPassword;

    @Schema(name  = "联系人")
    private String cnname;

    @Schema(name  = "联系方式(手机号,电话号)")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}", message = "手机号格式错误")
    private String mobile;

    @Schema(name  = "企业名称")
    private String entName;

    @Schema(name  = "所属地区CODE")
    private String dictionaryCode;

    @Schema(name  = "用户角色id")
    private String roleId;

    @Schema(name  = "用户角色CODE")
    private String roleCode;

    @Schema(name  = "用户角色")
    private String roleName;

    @Schema(name  = "开始时间")
    private Date opentime;

    @Schema(name  = "过期时间")
    private Date expiredtime;

    @Schema(name  = "微信openId")
    private String openId;

    @Schema(name  = "账号状态(0停用,1启用)")
    private String userstatus;

    @Schema(name  = "是否可用(逻辑删除，0不可用，1可用)")
    private String status;

    @Schema(name  = "用户地区类型(1.地区，2.园区)")
    private String districtType;

}
