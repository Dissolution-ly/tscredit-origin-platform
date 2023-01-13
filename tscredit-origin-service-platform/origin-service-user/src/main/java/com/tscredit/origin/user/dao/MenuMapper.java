package com.tscredit.origin.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    List<Map<String, Object>> getListMap(@Param("name") String name);

    List<Map<String, Object>> getUserMenu(@Param("roleCode") Integer roleId);
}
