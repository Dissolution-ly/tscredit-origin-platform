package com.tscredit.origin.user.entity.dao;

import com.aurora.base.entity.BaseDto;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_sys_data_permission_user")
@Schema(name="DataPermissionUser对象", description="")
public class DataPermissionUser extends BaseDto {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "机构id")
    private Long organizationId;

    @Schema(name = "状态 0禁用，1 启用,")
    private Integer status;


}
