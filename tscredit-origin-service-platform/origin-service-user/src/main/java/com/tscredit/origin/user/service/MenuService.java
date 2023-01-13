package com.tscredit.origin.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author lixuanyu
 * @since 2021-08-11
 */
public interface MenuService extends IService<Menu> {

    List<Map<String, Object>> getListMap(String name);

    List<Map<String, Object>> getUserMenu(String roleCode);

}
