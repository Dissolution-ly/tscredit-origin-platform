package com.tscredit.origin.user.entity.dao;

import com.aurora.base.entity.BaseDto;
import com.aurora.boot.validataed.Flag;
import com.aurora.boot.validataed.PassWord;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Date;

@Data
@TableName("urm_user")
@Schema(name = "User对象", description = "User对象")
public class User extends BaseDto {

    private static final long serialVersionUID = 1L;

    @Schema(name = "用户id")
    @TableId(value = "fu_id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "登录账号")
    @TableField(value = "fu_loginname")
    @NotBlank(message = "账号不能为空")
    @Size(min = 4, max = 20, message = "账号长度不能小于{min}位，且不能大于{max}位")
    private String loginName;

    @Schema(name = "登录密码")
    @TableField(value = "fu_password")
    @NotBlank(message = "密码不能为空")
    @PassWord
    private String password;

    @Schema(name = "头像")
    @TableField(value = "fu_avatar")
    @Pattern(regexp = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\?{0,1}(([A-Za-z0-9-~]+\\={0,1})([A-Za-z0-9-~]*)\\&{0,1})*)$", message="头像图片地址格式不正确")
    private String avatar;

    @Schema(name = "用户名称(昵称)")
    @TableField(value = "fu_name")
    @Size(max = 20, message = "用户名称长度不能大于{max}位")
    private String userName;

    @Schema(name = "用户性别")
    @TableField(value = "fu_gender")
    @Flag(values = {"1", "2"},message = "非法性别选项")
    private String gender;

    @Schema(name = "联系人")
    @TableField(value = "fu_cnname")
    @NotBlank(message = "联系人不能为空")
    @Size(max = 20, message = "联系人长度不能大于{max}位")
    private String cnname;

    @Schema(name = "邮箱")
    @TableField(value = "fu_email")
    @Email
    private String email;

    @Schema(name = "联系方式(手机号,电话号)")
    @TableField(value = "fu_mobile")
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}", message = "手机号格式错误")
    private String mobile;

    @Schema(name = "企业名称")
    @TableField(value = "fu_ent_name")
    @Size(max = 20, message = "企业名称长度不能大于{max}位")
    private String entName;

    @Schema(name = "所属地区CODE")
    @TableField(value = "fu_district_code")
    @Size(max = 20, message = "所属地区CODE长度不能大于{max}位")
    private String dictionaryCode;

    @Schema(name = "用户地区类型(1.地区，2.园区)")
    @TableField(value = "fu_district_type")
    @Flag(values = {"1", "2"}, message = "地区类型错误")
    private String districtType;

    @Schema(name = "街道详细地址")
    @TableField(value = "fu_district_street")
    @Size(max = 200, message = "街道详细地址输入过长")
    private String street;

    @Schema(name = "开始时间")
    @TableField(value = "fu_opentime")
    @NotNull(message = "开通时间不能为空")
    private Date opentime;

    @Schema(name = "过期时间")
    @TableField(value = "fu_expiredtime")
    @NotNull(message = "过期时间不能为空")
    private Date expiredtime;

    @Schema(name = "账号状态(0停用,1启用)")
    @TableField(value = "fu_userstatus")
    @Flag(values = {"2", "1", "0"}, message = "输入的账号状态异常")
    private String userStatus;

    @Schema(name = "备注")
    @TableField(value = "fu_comments")
    @Size(max = 200, message = "备注输入过长")
    private String comments;


}
