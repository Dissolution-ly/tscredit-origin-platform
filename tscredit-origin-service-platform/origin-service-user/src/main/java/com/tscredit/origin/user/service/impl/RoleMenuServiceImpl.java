package com.tscredit.origin.user.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.user.dao.RoleMenuMapper;
import com.tscredit.origin.user.entity.RoleMenu;
import com.tscredit.origin.user.service.RoleMenuService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    private final RoleMenuMapper roleMenuMapper;

    public RoleMenuServiceImpl(RoleMenuMapper roleMenuMapper) {
        this.roleMenuMapper = roleMenuMapper;
    }

    public QueryWrapper<RoleMenu> getWrapper(RoleMenu roleMenu) {
        QueryWrapper<RoleMenu> wrapper = new QueryWrapper<>();
        if (roleMenu == null) return null;

        //
        if (roleMenu.getId() != null) {
            wrapper.eq("id", roleMenu.getId());
        }
        //角色编码
        if (roleMenu.getRoleCode() != null) {
            wrapper.eq("role_code", roleMenu.getRoleCode());
        }
        //菜单编码
        if (roleMenu.getMenuCode() != null) {
            wrapper.eq("menu_code", roleMenu.getMenuCode());
        }

        return wrapper;
    }


    @Override
    public Page<RoleMenu> pageList(Page<RoleMenu> page, RoleMenu roleMenu) {
        return page(page, getWrapper(roleMenu));
    }
    @Override
    public List<String> selectRoleMenu(String roleCode, String type) {
        return roleMenuMapper.selectRoleMenu(roleCode, type);
    }

    @Override
    public List<Map<String, Object>> selectRoleMenu(String roleCode) {
        return roleMenuMapper.selectRoleMenuList(roleCode);
    }


}
