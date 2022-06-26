package com.tscredit.origin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.dao.Role;


public interface RoleService extends IService<Role> {

    Page<Role> pageList(Page<Role> page, Role role);

    Role getRoleByRoleCode(String roleCode);


}
