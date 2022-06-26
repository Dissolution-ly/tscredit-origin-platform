
package com.tscredit.origin.user.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户查询
 * </p>
 *
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "QueryUser对象", description = "用户查询")
public class QueryUserDTO implements Serializable
{
    
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "登录账号")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "用户名称(昵称)")
    private String userName;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "联系人")
    private String cnName;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "联系方式(手机号,电话号)")
    private String mobile;

    @ApiModelProperty(value = "企业名称")
    private String entName;

    @ApiModelProperty(value = "所属地区CODE")
    private String dictionaryCode;

    @ApiModelProperty(value = "用户地区类型(1.地区，2.园区)")
    private String districtType;

    @ApiModelProperty(value = "街道详细地址")
    private String street;

    @ApiModelProperty(value = "开始时间")
    private Date openTime;

    @ApiModelProperty(value = "过期时间")
    private Date expiredTime;

    @ApiModelProperty(value = "账号状态(0停用,1启用)")
    private String userStatus;

    @ApiModelProperty(value = "备注")
    private String comments;
    
    /**
     * vue级联选择
     */
    @ApiModelProperty(value = "地址数组")
    private List<String> areas;

    @ApiModelProperty(value = "角色id")
    private Long roleId;

    @ApiModelProperty(value = "开始时间")
    private String beginDateTime;

    @ApiModelProperty(value = "结束时间")
    private String endDateTime;

    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;
}
