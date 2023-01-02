package com.tscredit.origin.user.entity.dao;

import com.aurora.base.entity.BaseDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;



@Data
@ToString
@TableName("urm_role")
@ApiModel(value="Role对象", description="角色")
public class Role extends BaseDto {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父id")
    @TableField("parent_id")
    private Long parentId;

    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @TableField("role_name")
    private String name;

    @ApiModelProperty(value = "角色code")
    @TableField("role_code")
    private String code;

    @ApiModelProperty(value = "角色级别")
    @TableField("role_level")
    private Integer roleLevel;

    @ApiModelProperty(value = "1有效，0禁用")
    @TableField("role_status")
    private Integer roleStatus;

    @ApiModelProperty(value = "备注")
    @TableField("comments")
    private String comments;

    @ApiModelProperty(value = "角色数据权限")
    @TableField("data_permission_type")
    private String dataPermissionType;

}
