package com.excelutils;

import com.loggerutil.BaseLogger;
import com.readproperties.ReadProperties;

import java.util.Properties;

/**
 * @Author: Athena
 * @Date: 2025-03-21
 * @Desc: 读取 Excel 路径（从配置文件）
 **/
public class GetExcelPath extends BaseLogger {

    /**
     * 从配置文件中读取 Excel 路径
     *
     * @param propertiesFileName       配置文件名
     * @param propertiesFileParamsName Excel 路径对应的 key
     * @return Excel 文件路径（字符串）
     */
    public static String getExcelPath(String propertiesFileName, String propertiesFileParamsName) {
        logger.info("开始读取 Excel 路径配置: 文件名=" + propertiesFileName + ", key=" + propertiesFileParamsName);

        Properties properties = ReadProperties.readProperties(propertiesFileName);
        if (properties == null || properties.isEmpty()) {
            logger.error("未读取到配置文件或配置为空: " + propertiesFileName);
            throw new RuntimeException("读取配置文件失败: " + propertiesFileName);
        }

        String excelPath = properties.getProperty(propertiesFileParamsName);
        if (excelPath == null || excelPath.trim().isEmpty()) {
            logger.error("Excel 路径未配置或为空，key=" + propertiesFileParamsName);
            throw new RuntimeException("Excel 路径未配置或为空，key=" + propertiesFileParamsName);
        }

        logger.info("成功读取 Excel 路径: " + excelPath);
        return excelPath;
    }
}
