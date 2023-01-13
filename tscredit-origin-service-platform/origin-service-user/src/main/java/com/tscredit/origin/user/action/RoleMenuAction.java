package com.tscredit.origin.user.action;

import com.aurora.base.constant.ErrorMessage;
import com.aurora.base.entity.response.ActionMessage;
import com.aurora.base.exception.LogicException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.tscredit.origin.user.entity.RoleMenu;
import com.tscredit.origin.user.service.RoleMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;



/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@Tag(name ="角色管理", description = "RoleMenuAction")
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuAction {

    private final RoleMenuService roleMenuService;

    public RoleMenuAction(RoleMenuService roleMenuService) {
        this.roleMenuService = roleMenuService;
    }

    @Operation(summary = "保存角色-菜单关系")
    @PostMapping(value = "saveRoleMenu")
    public ActionMessage saveRoleMenuMapAction(@RequestParam String roleCode, @RequestParam List<String> menuIds) {
        QueryWrapper<RoleMenu> removeWrapper = new QueryWrapper<>();
        removeWrapper.eq("role_id", roleCode);
        roleMenuService.remove(removeWrapper);
        ArrayList<RoleMenu> roleMenus = new ArrayList<>();
        for (String menuId : menuIds) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuCode(menuId);
            roleMenu.setRoleCode(roleCode);
            roleMenus.add(roleMenu);
        }
        return ActionMessage.success().data(roleMenuService.saveBatch(roleMenus));
    }

    @Operation(summary = "根据角色Id,查询对应角色菜单url集合")
    @PostMapping(value = "selectRoleMenu")
    public ActionMessage selectRoleMenu(@RequestParam String roleCode, @RequestParam String type) {
        List<String> list = Lists.newArrayList("id", "url");
        if (!list.contains(type)) {
            throw LogicException.errorMessage(ErrorMessage.REQ_PARAM_ERROR);
        }
        return ActionMessage.success().data(roleMenuService.selectRoleMenu(roleCode, type));
    }


}
