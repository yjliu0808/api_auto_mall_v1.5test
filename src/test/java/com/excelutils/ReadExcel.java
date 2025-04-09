package com.excelutils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.constants.ExcelConstants;
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

    public static List<CaseInfo> readExcel( int startSheetIndex, int sheetNum) {
        // 设置读取参数
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(startSheetIndex);
        params.setSheetNum(sheetNum);

        // 自动关闭文件流，避免资源泄露
        try (FileInputStream fileInputStream = new FileInputStream(ExcelConstants.excelCasePath)) {
            logInfo("成功加载Excel文件: " + ExcelConstants.excelCasePath);

            List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(fileInputStream, CaseInfo.class, params);
            logInfo("读取Excel数据成功，记录条数: " + caseInfoList.size());
            return caseInfoList;

        } catch (IOException e) {
            logError("加载Excel文件失败: " + ExcelConstants.excelCasePath, e);
            throw new RuntimeException("加载Excel文件失败: " + ExcelConstants.excelCasePath, e);

        } catch (Exception e) {
            logError("解析Excel数据失败: " + ExcelConstants.excelCasePath, e);
            throw new RuntimeException("解析Excel数据失败: " + ExcelConstants.excelCasePath, e);
        }
    }
}
