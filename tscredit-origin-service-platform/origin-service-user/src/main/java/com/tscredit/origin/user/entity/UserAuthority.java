package com.tscredit.origin.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lixuanyu
 * @since 2021-08-16
 */
@Data
@ToString
@TableName("urm_user_authority")
@Schema(name  = "UserAuthority对象", description = "用户-权限")
public class UserAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name  = "用户Id")
    private String userId;

    @Schema(name  = "权限Id")
    private String authorityId;

    @Schema(name  = "权限内容")
    private String authorityContent;
}
