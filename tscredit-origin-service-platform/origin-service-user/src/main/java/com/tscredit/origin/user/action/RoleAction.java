package com.tscredit.origin.user.action;


import com.aurora.base.entity.response.ActionMessage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.Role;
import com.tscredit.origin.user.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@Tag(name ="角色", description = "RoleAction")
@RestController
@RequestMapping("/role")
public class RoleAction {

    private final RoleService roleService;
    public RoleAction(RoleService roleService){
        this.roleService = roleService;
    }

    @Operation(summary = "分页列表查询")
    @Parameters({
        @Parameter(name = "pageNum", description = "页数", required = true),
        @Parameter(name = "pageSize", description = "每页条数", required = true)
    })
    @PostMapping("/list")
    public ActionMessage list(@RequestParam Integer pageNum, @RequestParam Integer pageSize, Role role){
        return ActionMessage.success().data( roleService.pageList(new Page<>(pageNum, pageSize),role) );
    }

    @Operation(summary = "根据Id获取基本信息")
    @Parameters({
        @Parameter(name = "id", description = "id", required = true),
    })
    @PostMapping("/info")
    public ActionMessage info(String id){
        return ActionMessage.success().data(roleService.getById(id));
    }

    @Operation(summary = "保存/修改")
    @Parameters({
        @Parameter(name = "id", description = "id"),
    })
    @PostMapping("/saveOrUpdate")
    public ActionMessage update(@Validated Role role){
        return ActionMessage.success().data( roleService.saveOrUpdate(role) );
    }

    @Operation(summary = "删除")
    @Parameters({
        @Parameter(name = "ids", description = "id集合(字符串数组)", required = true),
    })
    @PostMapping("/delete")
    public ActionMessage delete(@RequestParam List<String> ids){
        // 查询角色是否存在关联用户
        List<Map<String, Object>> userRole = roleService.getUserRoleByRoleId(ids);
        if (userRole.size() > 0) {
            return ActionMessage.error("2001").msg("选择的角色已被用户绑定，不允许删除").data(userRole);
        }

        return ActionMessage.success().data( roleService.removeByIds(ids) );
    }


    @Operation(summary = "角色名验重,true为重复")
    @PostMapping(value = "isDuplicateRoleName")
    public ActionMessage isDuplicateRoleName(String id, @RequestParam String name) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(id)){
            wrapper.eq("id", id);
        }
        if(StringUtils.isNotBlank(name)){
            wrapper.eq("name", name);
        }
        return ActionMessage.success().data(roleService.list(wrapper) != null);
    }
}
