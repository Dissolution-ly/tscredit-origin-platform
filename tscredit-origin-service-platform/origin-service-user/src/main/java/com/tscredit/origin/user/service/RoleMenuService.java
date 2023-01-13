package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.RoleMenu;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
public interface RoleMenuService extends IService<RoleMenu> {

    Page<RoleMenu> pageList(Page<RoleMenu> page, RoleMenu roleMenu);

    List<String> selectRoleMenu(String roleCode, String type);

    List<Map<String,Object>> selectRoleMenu(String roleCode);
}
