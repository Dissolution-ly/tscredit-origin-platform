package com.tscredit.origin.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Map<String, String>> loadResourceDefine(@Param("roleId") String roleId);

    List<Map<String, Object>> getUserRoleByRoleId(@Param("ids") List<String> ids);
}
