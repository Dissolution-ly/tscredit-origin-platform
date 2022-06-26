package com.tscredit.origin.user.service;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tscredit.origin.user.entity.UserAuthority;
import java.util.List;
import java.util.Map;


public interface UserAuthorityService extends IService<UserAuthority> {

    Page<UserAuthority> pageList(Page<UserAuthority> page, UserAuthority userAuthority);

    <T> Map<String, T> userAuthority(String userId, String authorityId);

    Map<Object, Object> userAuthority(String userId);

    List<String> selectAllAuthority();

    public boolean deleteAuthorityByUserIds(List<String> userIds);
}
