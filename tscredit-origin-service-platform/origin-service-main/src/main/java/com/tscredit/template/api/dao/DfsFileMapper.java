package com.tscredit.template.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tscredit.template.api.entity.DfsFile;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author lixuanyu
 * @since 2021-04-21
 */
@Repository
public interface DfsFileMapper extends BaseMapper<DfsFile> {

    @Select("select id from dfs_file where is_deleted = '0' and fid = #{0}")
    List<String> getIds(Integer fid);
}
