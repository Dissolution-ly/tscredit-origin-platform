package com.tscredit.origin.user.entity.dto;


import com.tscredit.platform.mybatis.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="DataPermissionUserDTO对象", description="")
public class DataPermissionUserDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "机构id")
    private Long organizationId;

    @ApiModelProperty(value = "状态 0禁用，1 启用,")
    private Integer status;


}
