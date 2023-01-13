用户管理

目 录：

[TOC]

------

负责用户 登录、额度、权限 的校验，对全局提供对应的查询方法



# 一、配置说明

项目自动创建该模块，不需要进行模块的赋值，仅需要把 `建表_用户角色菜单.sql` 内的 SQL 语句执行，进行表结构的创建。



# 二、提供的方法

## 1. 获取用户信息

`JwtUtil` 类提供两个 `静态方法` 用于获取 **当前登录** 的用户信息，发生错误时抛异常，有返回值即为成功。

```JAVA
public static void main(String[] args) {
        // 查询 token 中解密出的信息，！！！该方式获取的是用户登录时的信息，不保证为实时数据！！！，如下：
        // data.put("userId", user.getId());
        // data.put("name", user.getLoginName());
        // data.put("dictionaryCode", user.getDictionaryCode());
        // data.put("roleCode", user.getRoleCode());
        // data.put("time", DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        // data.put("uuid", uuid);
        Map<String, Object> token = JwtUtil.token(request);

        // 查询 用户信息 (内部从 token 中解密 loginName 并 查询数据库)，该方式获取的数据为最新信息
        User loginUser = JwtUtil.getLoginUser();
        }
```

## 2. 额度

额度功能需配置 `user.quota.check.enable=true` 开启。

Hint : 各个模板的 **拦截器** 上也应添加 `@ConditionalOnProperty("user.quota.check.enable")` 以便同步 开启/关闭 额度功能

额度ID由各个模块自行向数据库  `urm_quota` 表 **插入数据** 进行指定。如：

```sql
INSERT INTO `template`.`urm_quota` (`id`, `name`, `sort`, `type`, `default_value`) 
VALUES ('TEST_TESTQUOTA', '测试额度总量', 1, 'total', 1000);

-- 字段介绍
-- id(额度ID) ： 该字段自定义，用于进行 额度查询 和 修改
-- name(额度名称) ： 页面展示该值
-- sort(排序)	: 页面展示时，按该字段进行排序，注意不要和已有数据冲突
-- type(额度结算周期)： 取值：total、day
-- default_value(默认额度数)： 创建用户时，若未配置额度,则按照该值设置用户额度
```

使用方式：

```java
@Aspect
@Component
@ConditionalOnProperty("user.quota.check.enable")
public class QuotaAspect {

    @Autowired
    UserQuotaService userQuotaService;

    @Before("execution(* com.tscredit.template.api.action..*(..))")
    public void test() {
        Integer userId = new Integer(4);
        String quotaId = "TEST_TESTQUOTA";

        // 验证 `指定用户` 的 `指定额度` 是否足够
        userQuotaService.verifyQuota(userId, quotaId);

        // 扣除 `指定用户` 的 `指定额度` (默认扣除 1 ，可通过重载方法设置扣除额度数)
        userQuotaService.deductQuota(userId, quotaId);
        userQuotaService.deductQuota(userId, quotaId, 5);
    }
}
```

## 3. 权限
权限功能需配置 `user.authority.check.enable=true` 开启。

Hint : 各个模板的 **权限功能部分** 也应添加 `@ConditionalOnProperty("user.quota.check.enable")` 以便同步 开启/关闭 权限功能

权限ID由各个模块自行向数据库  `urm_authority` 表 **插入数据** 进行指定。如：

```sql
INSERT INTO `urm_authority` (`id`, `name`, `sort`) VALUES ('DSZSQ', '都市招商圈权限', 1);

-- 字段介绍
-- id(权限ID) ： 该字段自定义，用于进行 权限查询
-- name(权限名称) ： 页面展示该值
-- sort(排序)	: 页面展示时，按该字段进行排序，注意不要和已有数据冲突
```

使用方式：

```java
@Component
@ConditionalOnProperty("user.authority.check.enable")
public class AuthorityTest {

    @Autowired
    UserAuthorityService userAuthorityService;
    
    public void test() {
        String authorityId = "DSZSQ";

        // 查询指定权限：返回值样例 (不同模块定义可能有所差异，但必须为 Map 格式)
        // {
        //      "化工产业": "HGCYAN",
        //      "房地产业": "FDCY",
        //      "数字经济": "SZJJBZH",
        //      "文创产业": "WCCY",
        //      "数字经济": "SZJJ"
        // }
        Map<String,Object> data = userAuthorityService.userAuthority(userId, authorityId);
    }
}
```