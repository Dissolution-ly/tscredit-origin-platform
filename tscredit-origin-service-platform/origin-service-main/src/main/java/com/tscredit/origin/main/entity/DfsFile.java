package com.tscredit.origin.main.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Date;

/**
 * @author lixuanyu
 * @since 2021-04-21
 */

@TableName("dfs_file")
@Schema(name="DfsFile对象", description="文件表")
public class DfsFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键(自增长)")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @Schema(name = "文件访问地址")
    private String location;

    @Schema(name = "文件所属组")
    private String fileGroup;

    @Schema(name = "文件真实名称")
    private String realName;


    @Schema(name = "文件存储位置")
    private String site;

    @Schema(name = "服务id / 分类id")
    private Integer fid;

    @Schema(name = "排序")
    private Integer sort;

    @Schema(name = "类型")
    @JsonIgnore
    private String type;

    @Schema(name = "创建时间")
    @JsonIgnore
    private Date gmtCreate;

    @Schema(name = "修改时间")
    @JsonIgnore
    private Date gmtModified;

    @Schema(name = "是否删除 1删除 0可用（逻辑删除）")
    @TableLogic(value = "0", delval = "1")
    @JsonIgnore
    private Integer isDeleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFileGroup(String fileGroup) {
        this.fileGroup = fileGroup;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getFileGroup() {
        return fileGroup;
    }

    public String getRealName() {
        return realName;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getFid() {
        return fid;
    }

    public void setFid(Integer fid) {
        this.fid = fid;
    }
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }
    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }
    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "DfsFile{" +
            "id=" + id +
            ", location=" + location +
            ", fileGroup=" + fileGroup +
            ", realName=" + realName +
            ", site=" + site +
            ", fid=" + fid +
            ", sort=" + sort +
            ", type=" + type +
            ", gmtCreate=" + gmtCreate +
            ", gmtModified=" + gmtModified +
            ", isDeleted=" + isDeleted +
        "}";
    }
}
