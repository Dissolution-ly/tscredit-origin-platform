
package com.tscredit.service.user.client.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@Schema(name = "用户信息", description = "用户信息")
public class UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private Long id;

    @Schema(name = "租户id")
    private Long tenantId;

    @Schema(name = "账号")
    private String account;

    @Schema(name = "昵称")
    private String nickname;

    @Schema(name = "姓名")
    private String realName;

    @Schema(name = "1 : 男，0 : 女")
    private String gender;

    @Schema(name = "邮箱")
    private String email;

    @Schema(name = "电话")
    private String mobile;

    @Schema(name = "密码")
    private String password;

    @Schema(name = "'0'禁用,'1' 启用, '2' 密码过期或初次未修改")
    private String status;

    @Schema(name = "头像")
    private String avatar;

    @Schema(name = "国家")
    private String country;

    @Schema(name = "省")
    private String province;

    @Schema(name = "市")
    private String city;

    @Schema(name = "区")
    private String area;

    @Schema(name = "街道详细地址")
    private String street;

    @Schema(name = "备注")
    private String comments;

    @Schema(name = "角色id")
    private Long roleId;

    @Schema(name = "机构id")
    private Long organizationId;

    @Schema(name = "机构名称")
    private String organizationName;

    @Schema(name = "角色id集合")
    private String roleIds;

    @Schema(name = "角色标识")
    private String roleKey;

    @Schema(name = "角色名称")
    private String roleName;

    @Schema(name = "数据权限")
    private String dataPermission;

    @Schema(name = "角色列表")
    private List<String> roles;

    @Schema(name = "资源列表字符串")
    private List<String> stringResources;

}
