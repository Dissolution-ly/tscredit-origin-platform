package com.tscredit.origin.user.entity;

import com.aurora.boot.validataed.Flag;
import com.aurora.boot.validataed.PassWord;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@ToString
@TableName("urm_user")
@Schema(name  = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name  = "用户id")
    @TableId(value = "fu_id", type = IdType.AUTO)
    private Integer id;

    @Schema(name  = "用户名称(昵称)")
    @TableField(value = "fu_name")
    @Size(max = 20, message = "用户名称长度不能大于{max}位", groups = {User.Update.class, User.Insert.class, User.Login.class})
    private String userName;

    @Schema(name  = "登录账号")
    @TableField(value = "fu_loginname")
    @NotBlank(message = "账号不能为空", groups = {User.Update.class, User.Insert.class, User.Login.class})
    @Size(max = 20, message = "账号长度不能大于{max}位", groups = {User.Update.class, User.Insert.class, User.Login.class})
    private String loginName;

    @Schema(name  = "登录密码")
    @TableField(value = "fu_password", select = false)
    @NotBlank(message = "密码不能为空", groups = {User.Update.class, User.Insert.class, User.Login.class})
    @PassWord(groups = {User.UpdatePassword.class, User.Insert.class, User.Login.class})
    private String password;

    @Schema(name  = "重复输入密码,修改密码时验证")
    @TableField(exist = false)
    private String rePassword;

    @Schema(name  = "旧密码")
    @TableField(exist = false)
    private String oldPassword;

    @Schema(name  = "联系人")
    @TableField(value = "fu_cnname")
    @NotBlank(message = "联系人不能为空", groups = {User.Update.class, User.Insert.class})
    @Size(max = 20, message = "联系人长度不能大于{max}位", groups = {User.Update.class, User.Insert.class})
    private String cnname;

    @Schema(name  = "联系方式(手机号,电话号)")
    @TableField(value = "fu_mobile")
    @NotBlank(message = "手机号不能为空", groups = {User.Update.class, User.Insert.class})
    @Pattern(regexp = "1[3|4|5|7|8][0-9]\\d{8}", message = "手机号格式错误")
    private String mobile;

    @Schema(name  = "企业名称")
    @TableField(value = "fu_ent_name")
    @Size(max = 20, message = "企业名称长度不能大于{max}位", groups = {User.Update.class, User.Insert.class})
    private String entName;

    @Schema(name  = "所属地区CODE")
    @TableField(value = "fu_district_code")
    @Size(max = 20, message = "所属地区CODE长度不能大于{max}位", groups = {User.Update.class, User.Insert.class})
    private String dictionaryCode;

    @Schema(name  = "用户角色id")
    @TableField(value = "role_id")
    private String roleId;

    @Schema(name  = "用户角色CODE")
    @TableField(value = "(SELECT `role_code` from urm_role where id = role_id)",
            insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private String roleCode;

    @Schema(name  = "用户角色")
    @TableField(value = "(SELECT `role_name` from urm_role where id = role_id)",
            insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private String roleName;

    @Schema(name  = "开始时间")
    @TableField(value = "fu_opentime")
    @NotNull(message = "开通时间不能为空", groups = {User.Update.class, User.Insert.class})
    private Date opentime;

    @Schema(name  = "过期时间")
    @TableField(value = "fu_expiredtime")
    @NotNull(message = "过期时间不能为空", groups = {User.Update.class, User.Insert.class})
    private Date expiredtime;

    @Schema(name  = "微信openId")
    @TableField(value = "fu_open_id")
    private String openId;

    @Schema(name  = "账号状态(0停用,1启用)")
    @TableField(value = "fu_userstatus")
    @Flag(values = {"1", "0"}, message = "输入的账号状态异常")
    private String userstatus;

    @Schema(name  = "是否可用(逻辑删除，0不可用，1可用)")
    @TableField(value = "fu_status")
    @JsonIgnore
    private String status;

    @Schema(name  = "用户地区类型(1.地区，2.园区)")
    @TableField(value = "fu_district_type")
    private String districtType;

    public Date getOpentime() {
        return null == opentime ? null : (Date) opentime.clone();
    }

    public Date getExpiredtime() {
        return null == expiredtime ? null : (Date) expiredtime.clone();
    }

    public void setOpentime(Date opentime) {
        this.opentime = null == opentime ? null : (Date) opentime.clone();
    }

    public void setExpiredtime(Date expiredtime) {
        this.expiredtime = null == expiredtime ? null : (Date) expiredtime.clone();
    }

    public interface Login {
        // 登录参数验证(检查 账号,密码)
    }

    public interface Insert {
        // 新增修改参数验证
    }

    public interface UpdatePassword {
        // 修改参数验证()
    }

    public interface Update {
        // 修改参数验证()
    }

}
