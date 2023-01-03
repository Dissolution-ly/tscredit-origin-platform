package com.tscredit.origin.user.entity.dao;

import com.aurora.base.entity.BaseDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@TableName("urm_role_menu")
@Schema(name="RoleMenu对象", description="角色-菜单")
public class RoleResource extends BaseDto {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "角色编码")
    @TableField(value ="role_id")
    private String roleCode;

    @Schema(name = "菜单编码")
    @TableField(value ="menu_id")
    private String menuCode;

}
