package com.caseutils;

import com.excelutils.ReadExcel;
import com.entity.CaseInfo;
import com.loggerutil.BaseLogger;
import org.testng.annotations.DataProvider;

import java.util.List;

/**
 * @Author： Athena
 * @Date： 2025-03-20
 * @Desc： 提供测试数据（TestNG @DataProvider）
 **/
public class TestDataProvider extends BaseLogger {

    /**
     * 提供给 TestNG 的测试数据（从 Excel 读取）
     */
    @DataProvider(name = "testDataCaseInfo")
    public static Object[] testDataProvider() {
        // 读取 Excel 中的数据
        List<CaseInfo> caseInfoList = ReadExcel.readExcel(
                TestngXmlParameters.propertiesFileName,
                TestngXmlParameters.propertiesFileParamsName,
                TestngXmlParameters.startSheetIndex,
                TestngXmlParameters.sheetNum
        );

        if (caseInfoList == null || caseInfoList.isEmpty()) {
            logger.error("❌ 无法从 Excel 中读取到测试数据，请检查配置参数或 Excel 文件内容！");
            throw new RuntimeException("无法从 Excel 中读取到测试数据！");
        }

        // 转换为 Object[] 供 TestNG 使用
        Object[] dataArray = caseInfoList.toArray();

        logger.info("✅ 成功读取测试数据，共 " + dataArray.length + " 条！");
        return dataArray;
    }
}
