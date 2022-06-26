package com.tscredit.origin.user.action;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.common.response.ActionMessage;
import com.tscredit.origin.user.entity.dao.Role;
import com.tscredit.origin.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Api(tags = {"角色"}, value = "RoleAction")
@RestController
@RequestMapping("/role")
public class RoleAction {

    private final RoleService roleService;

    public RoleAction(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation("分页列表查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页数", dataType = "int", dataTypeClass = Integer.class, required = true, defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize", value = "每页条数", dataType = "int", dataTypeClass = Integer.class, required = true, defaultValue = "20")
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, Role role) {
        return ActionMessage.success().data(roleService.pageList(new Page<>(pageNum, pageSize), role));
    }

    @ApiOperation("根据Id获取基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "1"),
    })
    @PostMapping("/info")
    public ActionMessage info(String id) {
        return ActionMessage.success().data(roleService.getById(id));
    }

    @ApiOperation("保存/修改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", dataType = "string", dataTypeClass = String.class, required = true, defaultValue = "1"),
    })
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(@Validated Role role) {
        return ActionMessage.success().data(roleService.saveOrUpdate(role));
    }

    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "id集合(字符串数组)", dataType = "string", dataTypeClass = String.class, required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<String> ids) {
        return ActionMessage.success().data(roleService.removeByIds(ids));
    }


    @ApiOperation("角色名验重,true为重复")
    @PostMapping(value = "isDuplicateRoleName")
    public ActionMessage isDuplicateRoleName(String id, @RequestParam String name) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(id)) {
            wrapper.eq("id", id);
        }
        if (StringUtils.isNotBlank(name)) {
            wrapper.eq("name", name);
        }
        return ActionMessage.success().data(roleService.list(wrapper) != null);
    }
}
