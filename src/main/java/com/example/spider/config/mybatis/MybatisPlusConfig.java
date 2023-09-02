package com.example.spider.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.MultiDataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
@Slf4j
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        // 多租户插件配置,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存万一出现问题
        // 多租户 != 权限过滤,不要乱用,租户之间是完全隔离的!!!
        interceptor.addInnerInterceptor(getTenantLineInnerInterceptor());

        // 数据权限插件
//        interceptor.addInnerInterceptor(getDataPermissionInterceptor());

        // 如果用了分页插件注意先 add TenantLineInnerInterceptor 再 add PaginationInnerInterceptor
        // 建议使用如下顺序
        // 1. 多租户,动态表名
        // 2. 分页,乐观锁
        // 3. sql 性能规范,防止全表更新与删除
        PaginationInnerInterceptor pageInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        pageInnerInterceptor.setMaxLimit(-1L);
        interceptor.addInnerInterceptor(pageInnerInterceptor);

        return interceptor;
    }

    private TenantLineInnerInterceptor getTenantLineInnerInterceptor() {
        return new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                // LoginUserContextHolder.getUser() 全局中获取到当前用户
                return new StringValue("system_user");
            }

            @Override
            public String getTenantIdColumn() {
                return "tenant_id";
            }

            @Override
            public boolean ignoreTable(String tableName) {
                // LoginUserContextHolder.getUser() 获取不到用户，可以直接 return true
                // 表名可以从配置中获取，tenantProperties.getIgnoreTables().stream().anyMatch((e) -> e.equalsIgnoreCase(tableName))
                return Stream.of("t_fucai", "t_ticai").anyMatch((e) -> e.equalsIgnoreCase(tableName));
            }

            @Override
            public boolean ignoreInsert(List<Column> columns, String tenantIdColumn) {
                // columns是值不为空的所有字段，所以插入时 tenantId 字段不为空，会执行忽略
                return TenantLineHandler.super.ignoreInsert(columns, tenantIdColumn);
            }
        });
    }

    private DataPermissionInterceptor getDataPermissionInterceptor() {

        // 通过给SQL拼接Where语句，来控制SQL查询范围，来达到数据权限控制
        // 使用参考 DataPermissionInterceptorTest
        return new DataPermissionInterceptor(new MultiDataPermissionHandler() {
            @Override
            public Expression getSqlSegment(Table table, Expression where, String mappedStatementId) {
//                try {
//                    String sqlSegment = sqlSegmentMap.get(mappedStatementId, table.getName());
//                    if (sqlSegment == null) {
//                        log.info("{} {} AS {} : NOT FOUND", mappedStatementId, table.getName(), table.getAlias());
//                        return null;
//                    }
//                    Expression sqlSegmentExpression = CCJSqlParserUtil.parseCondExpression(sqlSegment);
//                    log.info("{} {} AS {} : {}", mappedStatementId, table.getName(), table.getAlias(), sqlSegmentExpression.toString());
//                    return sqlSegmentExpression;
//                } catch (JSQLParserException e) {
//                    e.printStackTrace();
//                }
                return null;
            }
        });

    }

}