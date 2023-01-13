package com.tscredit.origin.user.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@TableName("urm_role_menu")
@Schema(name ="RoleMenu对象", description="角色-菜单")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name  = "角色编码")
    @TableField(value ="role_id")
    private String roleCode;

    @Schema(name  = "菜单编码")
    @TableField(value ="menu_id")
    private String menuCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    @Override
    public String toString() {
        return "RoleMenu{" +
            "id=" + id +
            ", roleCode=" + roleCode +
            ", menuCode=" + menuCode +
        "}";
    }
}
