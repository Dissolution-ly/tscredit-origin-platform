package com.tscredit.origin.user.config;


import com.aurora.redis.config.RedisUtil;
import com.tscredit.origin.user.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author lixuanyu
 */
@Component
public class SecurityAuthorizationMetadata implements CommandLineRunner {

    private final RoleService roleService;
    private final RedisUtil redisUtil;

    public SecurityAuthorizationMetadata(RoleService roleService, RedisUtil redisUtil) {
        this.roleService = roleService;
        this.redisUtil = redisUtil;
    }

    @Override
    public void run(String... args) throws Exception {
        redisUtil.hmset(Constants.AUTHORITY_PREFIX + "role_url", roleService.loadResourceDefine());
    }


//    private static String preciseMatching(List<String> urls) {
//        AntPathMatcher pathMatcher = new AntPathMatcher();
//        for (int i = 0; i + 1 < urls.size(); i++) {
//            if (!pathMatcher.match(urls.get(i), urls.get(i + 1))) {
//                String temp = urls.get(i);
//                urls.set(i, urls.get(i + 1));
//                urls.set(i + 1, temp);
//            }
//        }
//
//        return urls.get(urls.size() - 1);
//    }
}
