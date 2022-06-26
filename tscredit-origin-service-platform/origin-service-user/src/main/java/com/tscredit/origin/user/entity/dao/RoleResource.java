package com.tscredit.origin.user.entity.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tscredit.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
@TableName("urm_role_menu")
@ApiModel(value="RoleMenu对象", description="角色-菜单")
public class RoleResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色编码")
    @TableField(value ="role_id")
    private String roleCode;

    @ApiModelProperty(value = "菜单编码")
    @TableField(value ="menu_id")
    private String menuCode;

}
