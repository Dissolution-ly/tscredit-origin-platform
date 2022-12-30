-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `urm_user`;
CREATE TABLE `urm_user`
(
    `fu_id`            int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `fu_name`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '用户名称(昵称)',
    `fu_loginname`     varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '登录账号',
    `fu_password`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '登录密码',
    `fu_cnname`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '联系人',
    `fu_mobile`        varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '联系方式(手机号,电话号)',
    `fu_ent_name`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '企业名称',
    `fu_district_code` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '所属地区CODE',
    `fu_district_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '用户地区类型 1.地区 2.园区',
    `role_id`          varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '用户角色CODE',
    `fu_opentime`      datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '开通时间',
    `fu_expiredtime`   datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '过期时间',
    `fu_userstatus`    varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci   NULL     DEFAULT '1' COMMENT '账号状态(0停用,1启用)',
    `fu_status`        varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci   NOT NULL DEFAULT '1' COMMENT '是否可用(逻辑删除，0不可用，1可用)',
    `fu_idt`           datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `fu_udt`           datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新日期',
    `fu_open_id`       varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '微信openID',
    PRIMARY KEY (`fu_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- 新增用户 root root@1
INSERT INTO `urm_user` (`fu_loginname`, `fu_password`, `fu_district_code`, `role_id`, `fu_opentime`, `fu_expiredtime`,
                        `fu_userstatus`, `fu_status`)
VALUES ('root', '{noop}root@1', '110000', '2', '2022-04-19 16:37:34', '2099-12-31 16:37:34', '1', '1');

-- ----------------------------
-- 微信用户信息
-- ----------------------------
DROP TABLE IF EXISTS `urm_user_info_wx`;
CREATE TABLE `urm_user_info_wx`
(
    `wx_openid`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin  NOT NULL,
    `wx_userinfo` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
    PRIMARY KEY (`wx_openid`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 角色表
-- ----------------------------
DROP TABLE IF EXISTS `urm_role`;
CREATE TABLE `urm_role`
(
    `id`        int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '角色名称',
    `role_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '角色CODE',
    `status`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT '1' COMMENT '是否可用：1可用，0停用',
    `idt`       datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `udt`       datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色'
  ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- 菜单表
-- ----------------------------
DROP TABLE IF EXISTS `urm_menu`;
CREATE TABLE `urm_menu`
(
    `id`             int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '菜单id',
    `p_id`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '父级菜单编码',
    `name`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '菜单名称',
    `url`            varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '菜单地址/访问路径',
    `type`           varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '菜单类型:1.菜单,2.功能,3.目录',
    `sort`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '菜单排序',
    `menu_code`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '前端专用字段',
    `authority`      varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '后端权限字段',
    `authority_info` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '后端权限字段解释',
    `status`         varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT '1' COMMENT '是否可用：1可用，0停用',
    `idt`            datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '插入时间',
    `udt`            datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '菜单'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 角色-菜单表
-- ----------------------------
DROP TABLE IF EXISTS `urm_role_menu`;
CREATE TABLE `urm_role_menu`
(
    `id`      int(11)                                                NOT NULL AUTO_INCREMENT,
    `role_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色编码',
    `menu_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单编码',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '角色-菜单'
  ROW_FORMAT = DYNAMIC;



-- ----------------------------
-- 额度
-- ----------------------------
DROP TABLE IF EXISTS `urm_quota`;
CREATE TABLE `urm_quota`
(
    `id`            varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '额度ID',
    `name`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '额度名称',
    `sort`          int(11)                                                 NULL DEFAULT NULL COMMENT '排序字段',
    `type`          varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'total表示该额度为总量  day表示该额度为单日',
    `default_value` int(11)                                                 NOT NULL COMMENT '默认额度',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '额度'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 用户额度
-- ----------------------------
DROP TABLE IF EXISTS `urm_user_quota`;
CREATE TABLE `urm_user_quota`
(
    `id`          int(11)                                                NOT NULL AUTO_INCREMENT,
    `user_id`     varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户/角色 id',
    `quota_id`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '额度id',
    `quota_total` int(11)                                                NULL DEFAULT NULL COMMENT '总额度',
    `quota_use`   int(11)                                                NULL DEFAULT 0 COMMENT '已用额度',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1473
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户-额度'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 权限表
-- ----------------------------
DROP TABLE IF EXISTS `urm_authority`;
CREATE TABLE `urm_authority`
(
    `id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限ID',
    `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
    `sort` DOUBLE                                                  NULL DEFAULT NULL COMMENT '排序字段',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '权限表'
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- 用户权限表
-- ----------------------------
DROP TABLE IF EXISTS `urm_user_authority`;
CREATE TABLE `urm_user_authority`
(
    `user_id`           varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户Id',
    `authority_id`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限Id',
    `authority_content` text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL DEFAULT NULL COMMENT '权限内容',
    PRIMARY KEY (`user_id`, `authority_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 8729
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户权限表'
  ROW_FORMAT = DYNAMIC;
