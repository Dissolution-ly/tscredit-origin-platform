package com.tscredit.origin.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.dao.RoleResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RoleMenuMapper extends BaseMapper<RoleResource> {

    List<String> selectRoleMenu(String roleCode, String type);

    List<Map<String, Object>> selectRoleMenuList(String roleCode);
}
