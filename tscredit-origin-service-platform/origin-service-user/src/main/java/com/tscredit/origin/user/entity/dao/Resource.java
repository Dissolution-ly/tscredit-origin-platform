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
@TableName("t_menu")
@Schema(name="Menu对象", description="菜单")
public class Resource extends BaseDto {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name = "父ID")
    @TableField("parent_id")
    private String pId;

    @Schema(name = "名称")
    @TableField("resource_name")
    private String name;

    @Schema(name = "标识")
    @TableField("resource_code")
    private String code;

    @Schema(name = "菜单类型:1. 菜单 2.菜单 3. 按钮 4.链接 5.功能")
    @TableField("resource_type")
    private String type;

    @Schema(name = "地址")
    @TableField("resource_url")
    private String url;

    @Schema(name = "访问路径")
    @TableField("resource_path")
    private String path;

    @Schema(name = "排序")
    @TableField("resource_sort")
    private String sort;

    @Schema(name = "后端权限字段")
    @TableField("resource_authority")
    private String authority;

    @Schema(name = "所有上级组织id的集合，便于机构查找")
    @TableField("resource_ancestors")
    private String ancestors;

    @Schema(name = "资源图标")
    @TableField("resource_icon")
    private String icon;

    @Schema(name = "资源级别")
    @TableField("resource_level")
    private String level;

    @Schema(name = "是否显示")
    @TableField("resource_show")
    private String show;

    @Schema(name = "是否缓存")
    @TableField("resource_cache")
    private String cache;

    @Schema(name = "页面名称")
    @TableField("page_name")
    private String pageName;

    @Schema(name = "0.禁用 1.有效")
    @TableField("resource_status")
    private String resourceStatus;

}
