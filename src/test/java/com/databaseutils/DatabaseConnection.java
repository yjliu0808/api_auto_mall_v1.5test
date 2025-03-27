package com.databaseutils;

import com.loggerutil.BaseLogger;
import com.readproperties.ReadProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @Author： Athena
 * @Date： 2025-03-17
 * @Desc： 数据库连接管理类（线程安全，支持连接状态校验）
 */
public class DatabaseConnection extends BaseLogger {

    // 静态变量，存储连接实例
    private static Connection connection = null;

    // 静态代码块加载数据库驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("数据库驱动加载成功！");
        } catch (ClassNotFoundException e) {
            logger.error("数据库驱动加载失败！", e);
            throw new RuntimeException("数据库驱动加载失败！", e);
        }
    }

    /**
     * 获取数据库连接（线程安全 + 自动校验连接状态）
     * @param propertiesFileName 数据库配置文件名
     * @return Connection 实例
     */
    public static Connection getConnection(String propertiesFileName) {
        if (!isValidConnection()) {
            synchronized (DatabaseConnection.class) {
                if (!isValidConnection()) {
                    try {
                        Properties properties = ReadProperties.readProperties(propertiesFileName);
                        String url = properties.getProperty("db.url");
                        String username = properties.getProperty("db.username");
                        String password = properties.getProperty("db.password");

                        connection = DriverManager.getConnection(url, username, password);
                        logger.info("数据库连接成功！");
                    } catch (SQLException e) {
                        logger.error("数据库连接失败！", e);
                        throw new RuntimeException("数据库连接失败！", e);
                    }
                }
            }
        }
        return connection;
    }

    /**
     * 关闭数据库连接
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("数据库连接已关闭！");
            } catch (SQLException e) {
                logger.error("数据库连接关闭失败", e);
            } finally {
                connection = null;
            }
        }
    }

    /**
     * 校验当前连接是否有效
     * @return true 表示连接可用
     */
    private static boolean isValidConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            logger.warn("检查数据库连接状态失败", e);
            return false;
        }
    }
}
