package com.tscredit.origin.main.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * (CharMap)实体类
 *
 * @author lixuanyu
 * @since 2022-11-20 12:13:09
 */

@Data
@ToString
@EqualsAndHashCode
@TableName("api_char_map")
@Schema(name = "CharMap对象", description = "")
public class CharMap implements Serializable {
    private static final long serialVersionUID = -62592597989096114L;


    @TableId(value = "id")
    private String id;


    @Schema(name = "名称")
    @TableField(value = "name")
    private String name;

    @Schema(name = "父 ID")
    @TableField(value = "p_id")
    private String pId;

    @Schema(name = "父 名称")
    @TableField(value = "p_name")
    private String pName;

    @Schema(name = "筛选类别(不同位置)")
    @TableField(value = "type")
    private String type;

    @Schema(name = "筛选类别2(预留，某些地方可能需要二级筛选)")
    @TableField(value = "type2")
    private String type2;

    @Schema(name = "排序")
    @TableField(value = "sort")
    private Double sort;

    @Schema(name = "所属层级")
    @TableField(value = "level")
    private String level;

    @Schema(name = "拓展，预留字段")
    @TableField(value = "expand")
    private String expand;


}

