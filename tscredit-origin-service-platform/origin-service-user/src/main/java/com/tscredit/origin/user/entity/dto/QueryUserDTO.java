
package com.tscredit.origin.user.entity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "QueryUser对象", description = "用户查询")
public class QueryUserDTO implements Serializable
{
    
    private static final long serialVersionUID = 1L;

    @Schema(name = "用户id")
    private Long id;

    @Schema(name = "登录账号")
    private String loginName;

    @Schema(name = "登录密码")
    private String password;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "用户名称(昵称)")
    private String userName;

    @Schema(name = "用户性别")
    private String gender;

    @Schema(name = "联系人")
    private String cnName;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "联系方式(手机号,电话号)")
    private String mobile;

    @Schema(name = "企业名称")
    private String entName;

    @Schema(name = "所属地区CODE")
    private String dictionaryCode;

    @Schema(name = "用户地区类型(1.地区，2.园区)")
    private String districtType;

    @Schema(name = "街道详细地址")
    private String street;

    @Schema(name = "开始时间")
    private Date openTime;

    @Schema(name = "过期时间")
    private Date expiredTime;

    @Schema(name = "账号状态(0停用,1启用)")
    private String userStatus;

    @Schema(name = "备注")
    private String comments;
    
    /**
     * vue级联选择
     */
    @Schema(name = "地址数组")
    private List<String> areas;

    @Schema(name = "角色id")
    private Long roleId;

    @Schema(name = "开始时间")
    private String beginDateTime;

    @Schema(name = "结束时间")
    private String endDateTime;

    @Schema(name = "组织机构id")
    private Long organizationId;
}
