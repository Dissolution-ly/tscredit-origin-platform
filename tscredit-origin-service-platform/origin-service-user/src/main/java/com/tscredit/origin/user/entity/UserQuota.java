package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@TableName("urm_user_quota")
@Data
@ToString
@Schema(name = "UserQuota对象", description = "用户-额度")
public class UserQuota implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "用户/角色 id")
    private String userId;

    @Schema(name = "额度id")
    private String quotaId;

    @Schema(name = "总额度")
    private Integer quotaTotal;

    @Schema(name = "已用额度")
    private Integer quotaUse;
}
