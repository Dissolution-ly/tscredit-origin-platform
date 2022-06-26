package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@TableName("urm_user_quota")
@Data
@ToString
@ApiModel(value="UserQuota对象", description="用户-额度")
public class UserQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "用户/角色 id")
    private String userId;

    @ApiModelProperty(value = "额度id")
    private String quotaId;

    @ApiModelProperty(value = "总额度")
    private Integer quotaTotal;

    @ApiModelProperty(value = "已用额度")
    private Integer quotaUse;
}
