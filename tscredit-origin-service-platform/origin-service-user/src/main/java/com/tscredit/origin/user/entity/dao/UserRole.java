package com.tscredit.origin.user.entity.dao;

import com.aurora.base.entity.BaseDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 用户和角色关联表
 * </p>
 *
 * @since 2019-10-24
 */
@Data
@TableName("t_sys_user_role")
@Schema(name="UserRole对象", description="用户和角色关联表")
public class UserRole extends BaseDto {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "用户id")
    @TableField("user_id")
    private Long userId;

    @Schema(name = "角色id")
    @TableField("role_id")
    private Long roleId;
}
