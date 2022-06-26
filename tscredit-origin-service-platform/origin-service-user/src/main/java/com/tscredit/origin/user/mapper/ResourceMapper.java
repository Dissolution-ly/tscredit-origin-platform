package com.tscredit.origin.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.origin.user.entity.dao.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

    List<Map<String, Object>> getListMap(@Param("name") String name);

    List<Map<String, Object>> getUserMenu(@Param("roleCode") Integer roleId);


}
