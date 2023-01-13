package com.tscredit.origin.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


@TableName("urm_role")
@Data
@ToString
@Schema(name ="Role对象", description="角色")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name  = "角色ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name  = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @TableField("role_name")
    private String name;

    @Schema(name  = "角色code")
    @TableField("role_code")
    private String code;

    @Schema(name  = "是否可用：1可用，0停用")
    @JsonIgnore
    private String status;
}
