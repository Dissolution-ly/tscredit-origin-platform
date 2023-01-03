package com.tscredit.origin.user.entity.dto;


import com.aurora.base.entity.BaseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author GitEgg
 * @since 2021-05-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name="DataPermissionUserDTO对象", description="")
public class DataPermissionUserDTO extends BaseDto {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    private Long id;

    @Schema(name = "用户id")
    private Long userId;

    @Schema(name = "机构id")
    private Long organizationId;

    @Schema(name = "状态 0禁用，1 启用,")
    private Integer status;


}
