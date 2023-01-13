package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@TableName("urm_menu")
@Schema(name ="Menu对象", description="菜单")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name  = "菜单id")
    @TableId(value = "id", type = IdType.AUTO)
    private String id;

    @Schema(name  = "父级菜单编码")
    private String pId;

    @Schema(name  = "菜单名称")
    private String name;

    @Schema(name  = "菜单地址/访问路径")
    private String url;

    @Schema(name  = "菜单类型:1.菜单,2.功能,3.目录")
    private String type;

    @Schema(name  = "菜单排序")
    private String sort;

    @Schema(name  = "前端专用字段")
    private String menuCode;

    @Schema(name  = "后端权限字段")
    private String authority;

    @Schema(name  = "后端权限字段解释")
    @TableField("authority_info")
    private String authorityInfo;

    @Schema(name  = "是否可用：1可用，0停用")
    private String status;

    @Schema(name  = "插入时间")
    private LocalDateTime idt;

    @Schema(name  = "修改时间")
    private LocalDateTime udt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public LocalDateTime getIdt() {
        return idt;
    }

    public void setIdt(LocalDateTime idt) {
        this.idt = idt;
    }
    public LocalDateTime getUdt() {
        return udt;
    }

    public void setUdt(LocalDateTime udt) {
        this.udt = udt;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", pId=" + pId +
            ", name=" + name +
            ", url=" + url +
            ", type=" + type +
            ", sort=" + sort +
            ", menuCode=" + menuCode +
            ", authority=" + authority +
            ", status=" + status +
            ", idt=" + idt +
            ", udt=" + udt +
        "}";
    }
}
