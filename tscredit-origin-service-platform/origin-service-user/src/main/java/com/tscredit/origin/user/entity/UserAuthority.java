package com.tscredit.origin.user.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@TableName("t_user_authority")
@ApiModel(value = "UserAuthority对象", description = "用户-权限")
public class UserAuthority implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "权限Id")
    private String authorityId;

    @ApiModelProperty(value = "权限内容")
    private String authorityContent;
}
