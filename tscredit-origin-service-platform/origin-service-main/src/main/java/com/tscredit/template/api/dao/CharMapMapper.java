package com.tscredit.template.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.template.api.entity.CharMap;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * (CharMap)表数据库访问层
 *
 * @author lixuanyu
 * @since 2022-11-20 12:13:09
 */

@Repository
public interface CharMapMapper extends BaseMapper<CharMap> {

}

