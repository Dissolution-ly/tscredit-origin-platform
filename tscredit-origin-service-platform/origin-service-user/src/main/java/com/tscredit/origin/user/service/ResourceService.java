package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.dao.Resource;

import java.util.List;
import java.util.Map;


public interface ResourceService extends IService<Resource> {

    List<Map<String, Object>> getListMap(String name);

    List<Map<String, Object>> getUserMenu(String roleCode);

}
