package com.tscredit.origin.user.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.dao.MenuMapper;
import com.tscredit.origin.user.entity.Menu;
import com.tscredit.origin.user.entity.Role;
import com.tscredit.origin.user.service.MenuService;
import com.tscredit.origin.user.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private final MenuMapper menuMapper;
    private final RoleService roleService;

    public MenuServiceImpl (MenuMapper menuMapper, RoleService roleService){
        this.menuMapper = menuMapper;
        this.roleService = roleService;
    }

    @Override
    public List<Map<String, Object>> getListMap(String name) {
        return menuMapper.getListMap(name);
    }


    @Override
    public List<Map<String, Object>> getUserMenu(String roleCode) {
        Role role = roleService.getRoleByRoleCode(roleCode);
        return menuMapper.getUserMenu(role.getId());
    }
}
