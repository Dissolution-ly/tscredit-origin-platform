
package com.tscredit.origin.user.entity.dto;

import com.tscredit.common.validataed.Flag;
import com.tscredit.common.validataed.PassWord;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p> 用户创建 </p>
 *
 * @since 2019-05-26
 */
@Data
@ApiModel(value = "CreateUser对象", description = "创建用户时的对象")
public class CreateUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "用户id")
    private Long id;

    @ApiModelProperty(value = "登录账号")
    @Pattern(regexp = "^[a-z0-9_-]{3,16}$", message = "账号格式不正确")
    private String loginName;

    @ApiModelProperty(value = "登录密码")
    @PassWord
    private String password;

    @ApiModelProperty(value = "头像")
    @Pattern(regexp = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$", message = "头像图片地址格式不正确")
    private String avatar;

    @ApiModelProperty(value = "用户名称(昵称)")
    @Size(max = 20, message = "用户名称长度不能大于{max}位")
    private String userName;

    @ApiModelProperty(value = "用户性别")
    @Flag(values = {"1", "2"}, message = "非法性别选项")
    private String gender;

    @ApiModelProperty(value = "联系人")
    @Size(min = 1, max = 20, message = "联系人长度不能小于{min}位，且不能大于{max}位")
    private String cnname;

    @ApiModelProperty(value = "邮箱")
    @Email
    private String email;

    @ApiModelProperty(value = "联系方式(手机号,电话号)")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}", message = "手机号格式错误")
    private String mobile;

    @ApiModelProperty(value = "企业名称")
    @Size(max = 20, message = "企业名称长度不能大于{max}位")
    private String entName;

    @ApiModelProperty(value = "所属地区CODE")
    @Size(max = 20, message = "所属地区CODE长度不能大于{max}位")
    private String dictionaryCode;

    @ApiModelProperty(value = "用户地区类型(1.地区，2.园区)")
    @Flag(values = {"1", "2"}, message = "地区类型错误")
    private String districtType;

    @ApiModelProperty(value = "街道详细地址")
    @Size(max = 200, message = "街道详细地址输入过长")
    private String street;

    @ApiModelProperty(value = "开始时间")
    @NotNull(message = "开通时间不能为空")
    private Date opentime;

    @ApiModelProperty(value = "过期时间")
    @NotNull(message = "过期时间不能为空")
    private Date expiredtime;

    @ApiModelProperty(value = "账号状态(0停用,1启用)")
    @Flag(values = {"2", "1", "0"}, message = "输入的账号状态异常")
    private String userStatus;

    @ApiModelProperty(value = "备注")
    @Size(min = 0, max = 255, message = "备注信息长度不正确")
    private String comments;


    @ApiModelProperty(value = "用户地区ID数组")
    private List<String> areas;

    @ApiModelProperty(value = "组织机构id")
    private Long organizationId;

    @ApiModelProperty(value = "用户角色id数组")
    private List<Long> roleIds;
}
