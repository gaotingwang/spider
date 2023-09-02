package com.example.spider.config.mybatis;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.DataPermissionInterceptor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.util.HashMap;
import java.util.Map;


public class DataPermissionInterceptorTest {

    private static String TEST_1 = "com.baomidou.userMapper.selectByUsername";
    private static String TEST_2 = "com.baomidou.userMapper.selectById";
    private static String TEST_3 = "com.baomidou.roleMapper.selectByCompanyId";
    private static String TEST_4 = "com.baomidou.roleMapper.selectById";
    private static String TEST_5 = "com.baomidou.roleMapper.selectByRoleId";

    /**
     * 这里可以理解为数据库配置的数据权限规则 SQL
     */
    private static Map<String, String> sqlSegmentMap = new HashMap<String, String>() {
        {
            put(TEST_1, "username='123' or userId IN (1,2,3)");
            put(TEST_2, "u.state=1 and u.amount > 1000");
            put(TEST_3, "companyId in (1,2,3)");
            put(TEST_4, "username like 'abc%'");
            put(TEST_5, "id=1 and role_id in (select id from sys_role)");
        }
    };

    private static DataPermissionInterceptor interceptor = new DataPermissionInterceptor(new DataPermissionHandler() {

        @Override
        public Expression getSqlSegment(Expression where, String mappedStatementId) {
            try {
                String sqlSegment = sqlSegmentMap.get(mappedStatementId);
                Expression sqlSegmentExpression = CCJSqlParserUtil.parseCondExpression(sqlSegment);
                if (null != where) {
                    if (mappedStatementId.equals(TEST_4)) {
                        // 这里测试返回 OR 条件
                        return new OrExpression(where, sqlSegmentExpression);
                    }
                    return new AndExpression(where, sqlSegmentExpression);
                }
                return sqlSegmentExpression;
            } catch (JSQLParserException e) {
                e.printStackTrace();
            }
            return null;
        }
    });

    public static void main(String[] args) {
        test3();
    }

    static void assertSql(String mappedStatementId, String sql, String targetSql) {
        System.out.println("拼接SQL:" + interceptor.parserSingle(sql, mappedStatementId));
        System.out.println("期望SQL:" + targetSql);
    }

    static void test1() {
        assertSql(TEST_1, "select * from sys_user",
                "SELECT * FROM sys_user WHERE username = '123' OR userId IN (1, 2, 3)");
    }

    static void test2() {
        assertSql(TEST_2, "select u.username from sys_user u join sys_user_role r on u.id=r.user_id where r.role_id=3",
                "SELECT u.username FROM sys_user u JOIN sys_user_role r ON u.id = r.user_id WHERE r.role_id = 3 AND u.state = 1 AND u.amount > 1000");
    }

    static void test3() {
        assertSql(TEST_3, "select * from sys_role where company_id=6",
                "SELECT * FROM sys_role WHERE company_id = 6 AND companyId IN (1, 2, 3)");
    }

    static void test3unionAll() {
        assertSql(TEST_3, "select * from sys_role where company_id=6 union all select * from sys_role where company_id=7",
                "SELECT * FROM sys_role WHERE company_id = 6 AND companyId IN (1, 2, 3) UNION ALL SELECT * FROM sys_role WHERE company_id = 7 AND companyId IN (1, 2, 3)");
    }

    static void test4() {
        assertSql(TEST_4, "select * from sys_role where id=3",
                "SELECT * FROM sys_role WHERE id = 3 OR username LIKE 'abc%'");
    }

    static void test5() {
        assertSql(TEST_5, "select * from sys_role where id=3",
                "SELECT * FROM sys_role WHERE id = 3 AND id = 1 AND role_id IN (SELECT id FROM sys_role)");
    }

}