package com.databaseutils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author: Athena
 * @Date: 2025-03-22
 * @Desc: 数据库查询工具类，支持单值查询
 **/
public class SqlUtils {

    private static final Logger logger = LoggerFactory.getLogger(SqlUtils.class);
    private static final String DEFAULT_CONFIG_PATH = "/database.properties";

    /**
     * 查询单个结果值（默认配置路径）
     */
    public static Object querySingleValue(String sql, Object... params) {
        return querySingleValue(DEFAULT_CONFIG_PATH, sql, params);
    }

    /**
     * 查询单个结果值（指定配置路径）
     */
    public static Object querySingleValue(String configPath, String sql, Object... params) {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection(configPath);
            QueryRunner queryRunner = new QueryRunner();
            ScalarHandler<Object> scalarHandler = new ScalarHandler<>();
            logger.debug("Executing SQL: {}, with params: {}", sql, params);
            return queryRunner.query(connection, sql, scalarHandler, params);
        } catch (SQLException e) {
            logger.error("SQL执行异常: {}", sql, e);
            throw new RuntimeException("执行 SQL 异常: " + sql, e);
        } finally {
            DbUtils.closeQuietly(connection);
        }
    }
}
