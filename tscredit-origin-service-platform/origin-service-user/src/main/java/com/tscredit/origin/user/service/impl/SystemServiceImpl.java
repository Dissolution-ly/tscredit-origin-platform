package com.tscredit.origin.user.service.impl;

import com.aurora.base.exception.LogicException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.dao.SystemTable;
import com.tscredit.origin.user.mapper.SystemTableMapper;
import com.tscredit.origin.user.service.SystemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
@AllArgsConstructor
public class SystemServiceImpl implements SystemService {

    private final SystemTableMapper systemTableMapper;

    @Override
    public List<SystemTable> list() {
        return systemTableMapper.list();
    }

    @Override
    public Page<SystemTable> page() {
        Page<SystemTable> page = new Page<>(1, 10);
        List<SystemTable> records = systemTableMapper.page(page);
        page.setRecords(records);
        return page;
    }

    @Override
    public String exception() {
        throw LogicException.errorMessage("自定义异常");
    }
}
