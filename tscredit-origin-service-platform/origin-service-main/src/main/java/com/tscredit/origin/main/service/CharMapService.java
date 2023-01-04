package com.tscredit.origin.main.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.main.entity.CharMap;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * (CharMap)表服务接口
 *
 * @author lixuanyu
 * @since 2022-11-20 12:13:20
 */
public interface CharMapService extends IService<CharMap> {

    /**
     * 分页查询
     *
     * @param charMap 筛选条件
     * @param page    分页对象
     * @return 查询结果
     */
    Page<CharMap> pageList(Page<CharMap> page, CharMap charMap);

}
