package com.excelutils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.entity.CaseInfo;
import com.loggerutil.BaseLogger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author： Athena
 * @Date： 2025-03-19
 * @Desc： 读取excel数据
 **/
public class ReadExcel extends BaseLogger {

    public static List<CaseInfo> readExcel(String propertiesFileName, String propertiesFileParamsName, int startSheetIndex, int sheetNum) {
        // 获取 Excel 文件路径
        String excelPath = GetExcelPath.getExcelPath(propertiesFileName, propertiesFileParamsName);
        logger.info("获取到Excel路径: " + excelPath);

        // 设置读取参数
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(startSheetIndex);
        params.setSheetNum(sheetNum);

        // 自动关闭文件流，避免资源泄露
        try (FileInputStream fileInputStream = new FileInputStream(excelPath)) {
            logger.info("成功加载Excel文件: " + excelPath);

            List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(fileInputStream, CaseInfo.class, params);
            logger.info("读取Excel数据成功，记录条数: " + caseInfoList.size());
            return caseInfoList;

        } catch (IOException e) {
            logger.error("加载Excel文件失败: " + excelPath, e);
            throw new RuntimeException("加载Excel文件失败: " + excelPath, e);

        } catch (Exception e) {
            logger.error("解析Excel数据失败: " + excelPath, e);
            throw new RuntimeException("解析Excel数据失败: " + excelPath, e);
        }
    }
}
