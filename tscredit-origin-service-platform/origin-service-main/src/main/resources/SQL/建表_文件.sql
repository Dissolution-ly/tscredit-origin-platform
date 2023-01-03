-- ----------------------------
-- 记录文件上传
-- ----------------------------
DROP TABLE IF EXISTS `dfs_file`;
CREATE TABLE `dfs_file`
(
    `id`           int(11)                                                 NOT NULL AUTO_INCREMENT COMMENT '主键(自增长)',
    `location`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '文件访问地址',
    `real_name`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '文件真实名称',
    `file_group`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '文件组',
    `site`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '文件存储名称',
    `fid`          int(11)                                                 NULL     DEFAULT NULL COMMENT '服务id / 分类id',
    `sort`         int(11)                                                 NULL     DEFAULT NULL COMMENT '排序',
    `type`         varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '类型',
    `gmt_create`   datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                                                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `is_deleted`   tinyint(3) UNSIGNED                                     NOT NULL DEFAULT 0 COMMENT '是否删除 1删除 0可用（逻辑删除）',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT = '文件表'
  ROW_FORMAT = DYNAMIC;


-- ----------------------------
-- 码表
-- 码表数据可参考文件 `码表数据.sql`
-- ----------------------------
DROP TABLE IF EXISTS `api_char_map`;
CREATE TABLE `api_char_map`
(
    `id`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `name`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
    `p_id`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父 ID',
    `p_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '父 名称',
    `type`   varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '筛选类别(不同位置)',
    `type2`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '筛选类别2(预留，某些地方可能需要二级筛选)',
    `sort`   double                                                  NULL DEFAULT NULL COMMENT '排序',
    `level`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属层级',
    `expand` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拓展，预留字段',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci R
    OW_FORMAT = DYNAMIC;