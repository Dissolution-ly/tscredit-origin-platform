package com.tscredit.origin.user.action;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.tscredit.common.response.ActionMessage;
import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.response.LogicException;
import com.tscredit.origin.user.entity.dao.RoleResource;
import com.tscredit.origin.user.service.RoleResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;



@Api(tags = {"角色管理"}, value = "RoleMenuAction")
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuAction {

    private final RoleResourceService roleResourceService;

    public RoleMenuAction(RoleResourceService roleResourceService) {
        this.roleResourceService = roleResourceService;
    }

    @ApiOperation("保存角色-菜单关系")
    @PostMapping(value = "saveRoleMenu")
    public ActionMessage saveRoleMenuMapAction(@RequestParam String roleCode, @RequestParam List<String> menuIds) {
        QueryWrapper<RoleResource> removeWrapper = new QueryWrapper<>();
        removeWrapper.eq("role_id", roleCode);
        roleResourceService.remove(removeWrapper);
        ArrayList<RoleResource> roleResources = new ArrayList<>();
        for (String menuId : menuIds) {
            RoleResource roleResource = new RoleResource();
            roleResource.setMenuCode(menuId);
            roleResource.setRoleCode(roleCode);
            roleResources.add(roleResource);
        }
        return ActionMessage.success().data(roleResourceService.saveBatch(roleResources));
    }

    @ApiOperation("根据角色Id,查询对应角色菜单url集合")
    @PostMapping(value = "selectRoleMenu")
    public ActionMessage selectRoleMenu(@RequestParam String roleCode, @RequestParam String type) {
        List<String> list = Lists.newArrayList("id", "url");
        if (!list.contains(type)) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        return ActionMessage.success().data(roleResourceService.selectRoleMenu(roleCode, type));
    }


}
