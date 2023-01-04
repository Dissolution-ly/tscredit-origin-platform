package com.tscredit.origin.main.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tscredit.origin.main.entity.CharMap;
import com.tscredit.origin.main.service.CharMapService;
import com.tscredit.origin.main.dao.CharMapMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * (CharMap)表服务实现类
 *
 * @author lixuanyu
 * @since 2022-11-20 12:13:22
 */

@Service("charMapService")
public class CharMapServiceImpl extends ServiceImpl<CharMapMapper, CharMap> implements CharMapService {

    private final CharMapMapper charMapMapper;

    public CharMapServiceImpl(CharMapMapper charMapMapper) {
        this.charMapMapper = charMapMapper;
    }

    public QueryWrapper<CharMap> getWrapper(CharMap charMap) {
        QueryWrapper<CharMap> wrapper = new QueryWrapper<>();
        if (charMap == null) {
            return wrapper;
        }

        // ${column.comment}
        if (StringUtils.isNotBlank(charMap.getId())) {
            wrapper.eq("id", charMap.getId());
        }

        // 名称
        if (StringUtils.isNotBlank(charMap.getName())) {
            wrapper.eq("name", charMap.getName());
        }

        // 父 ID
        if (StringUtils.isNotBlank(charMap.getPId())) {
            wrapper.eq("p_id", charMap.getPId());
        }

        // 父 名称
        if (StringUtils.isNotBlank(charMap.getPName())) {
            wrapper.eq("p_name", charMap.getPName());
        }

        // 筛选类别(不同位置)
        if (StringUtils.isNotBlank(charMap.getType())) {
            wrapper.eq("type", charMap.getType());
        }

        // 筛选类别2(预留，某些地方可能需要二级筛选)
        if (StringUtils.isNotBlank(charMap.getType2())) {
            wrapper.eq("type2", charMap.getType2());
        }

        // 排序
        if (charMap.getSort() != null) {
            wrapper.eq("sort", charMap.getSort());
        }

        // 所属层级
        if (StringUtils.isNotBlank(charMap.getLevel())) {
            wrapper.eq("level", charMap.getLevel());
        }

        // 拓展，预留字段
        if (StringUtils.isNotBlank(charMap.getExpand())) {
            wrapper.eq("expand", charMap.getExpand());
        }
        
        return wrapper;
    }


    @Override
    public Page<CharMap> pageList(Page<CharMap> page, CharMap charMap) {
        QueryWrapper<CharMap> wrapper = getWrapper(charMap);
        wrapper.orderByAsc("`sort`");
        return page(page, wrapper);
    }

}
