package com.tscredit.origin.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tscredit.origin.user.entity.dao.SystemTable;

import java.util.List;

public interface SystemService {

    List<SystemTable> list();

    Page<SystemTable> page();

    String exception();
}
