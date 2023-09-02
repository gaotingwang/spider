DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user
(
    id          BIGINT  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(30) NULL DEFAULT NULL COMMENT '姓名',
    age         INT NULL DEFAULT NULL COMMENT '年龄',
    email       VARCHAR(50) NULL DEFAULT NULL COMMENT '邮箱',
    grade       tinyint NOT NULL,
    deleted     tinyint NULL,
    tenant_id   VARCHAR(255) NULL,
    operator    VARCHAR(30) NULL DEFAULT NULL COMMENT '操作员',
    create_time bigint  NOT NULL COMMENT '创建时间',
    update_time bigint  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
);