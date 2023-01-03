package com.tscredit.origin.user.entity.dto;


import com.aurora.base.entity.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="DataPermissionUser对象", description="")
public class UpdateDataPermissionUserDTO extends BaseDto {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private Long id;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "机构id")
    private Long organizationId;

    @Schema(name = "状态 0禁用，1 启用,")
    private Integer status;

    @Schema(name = "需要添加的机构权限")
    private List<Long> addDataPermissions;

    @Schema(name = "需要删除的机构权限")
    private List<Long> removeDataPermissions;


}
