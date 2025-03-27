package com.readproperties;

import com.loggerutil.BaseLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author： Athena
 * @Date： 2025-03-19
 * @Desc： 读取类路径下的 properties 配置文件
 **/
public class ReadProperties extends BaseLogger {

    /**
     * 读取指定路径下的 properties 文件（例如 "/excel.properties"）
     *
     * @param propertiesFileName 配置文件名（带斜杠路径）
     * @return Properties 对象
     */
    public static Properties readProperties(String propertiesFileName) {
        logger.info("开始加载配置文件：" + propertiesFileName);

        try (InputStream inputStream = ReadProperties.class.getResourceAsStream(propertiesFileName)) {
            if (inputStream == null) {
                logger.error("未找到配置文件：" + propertiesFileName);
                throw new RuntimeException("未找到配置文件：" + propertiesFileName);
            }

            Properties properties = new Properties();
            properties.load(inputStream);
            logger.info("成功读取配置文件：" + propertiesFileName);
            return properties;

        } catch (IOException e) {
            logger.error("读取配置文件失败：" + propertiesFileName, e);
            throw new RuntimeException("读取配置文件失败：" + propertiesFileName, e);
        }
    }
}
