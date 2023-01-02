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


@Data
@ToString
@TableName("t_menu")
@ApiModel(value="Menu对象", description="菜单")
public class Resource extends BaseDto {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @ApiModelProperty(value = "父ID")
    @TableField("parent_id")
    private String pId;

    @ApiModelProperty(value = "名称")
    @TableField("resource_name")
    private String name;

    @ApiModelProperty(value = "标识")
    @TableField("resource_code")
    private String code;

    @ApiModelProperty(value = "菜单类型:1. 菜单 2.菜单 3. 按钮 4.链接 5.功能")
    @TableField("resource_type")
    private String type;

    @ApiModelProperty(value = "地址")
    @TableField("resource_url")
    private String url;

    @ApiModelProperty(value = "访问路径")
    @TableField("resource_path")
    private String path;

    @ApiModelProperty(value = "排序")
    @TableField("resource_sort")
    private String sort;

    @ApiModelProperty(value = "后端权限字段")
    @TableField("resource_authority")
    private String authority;

    @ApiModelProperty(value = "所有上级组织id的集合，便于机构查找")
    @TableField("resource_ancestors")
    private String ancestors;

    @ApiModelProperty(value = "资源图标")
    @TableField("resource_icon")
    private String icon;

    @ApiModelProperty(value = "资源级别")
    @TableField("resource_level")
    private String level;

    @ApiModelProperty(value = "是否显示")
    @TableField("resource_show")
    private String show;

    @ApiModelProperty(value = "是否缓存")
    @TableField("resource_cache")
    private String cache;

    @ApiModelProperty(value = "页面名称")
    @TableField("page_name")
    private String pageName;

    @ApiModelProperty(value = "0.禁用 1.有效")
    @TableField("resource_status")
    private String resourceStatus;

}
