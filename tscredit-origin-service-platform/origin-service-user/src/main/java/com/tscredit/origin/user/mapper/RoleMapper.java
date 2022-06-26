package com.tscredit.origin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.dao.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Map<String, String>> loadResourceDefine();
}
