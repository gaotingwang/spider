package com.example.spider.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * 自动填充功能
 * @author gtw
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        // 只有在属性上标注了 @TableField(fill = FieldFill.INSERT_UPDATE/FieldFill.INSERT) 才会对对应属性做赋值操作
        this.strictInsertFill(metaObject, "deleted", Integer.class, 0);
        this.strictInsertFill(metaObject, "operator", String.class, "Jetty");
        this.strictInsertFill(metaObject, "createTime", Long.class, System.currentTimeMillis());
        // 也可使用setFieldValByName
        Object updateTime = this.getFieldValByName("updateTime", metaObject);
        if (updateTime == null) {
            this.setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        // 只有在属性上标注了 @TableField(fill = FieldFill.INSERT_UPDATE/FieldFill.UPDATE) 才会对对应属性做赋值操作
        this.strictUpdateFill(metaObject, "operator", String.class, "Tom");

        // strictUpdateFill如果属性有值则不覆盖,可以使用setFieldValByName进行覆盖
        this.setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
    }
}