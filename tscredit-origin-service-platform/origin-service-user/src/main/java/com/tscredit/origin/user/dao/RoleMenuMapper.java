package com.tscredit.origin.user.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@Repository
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<String> selectRoleMenu(@Param("roleCode") String roleCode, @Param("type") String type);

    List<Map<String, Object>> selectRoleMenuList(@Param("roleCode") String roleCode);
}
