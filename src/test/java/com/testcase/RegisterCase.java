package com.testcase;

import com.entity.CaseInfo;
import com.excelutils.ReadExcel;
import com.parameters.RegisterParams;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("用户模块")
@Feature("注册功能")
public class RegisterCase extends BaseCase {

    /**
     * 注册接口测试方法，使用统一执行流程
     */
    @Test(description = "注册接口测试", dataProvider = "datas")
    public void testRegister(CaseInfo caseInfo) {
        executeTestCase(caseInfo, RegisterParams::paramsSetValue);
    }

    /**
     * 数据提供者：读取 Excel 注册用例
     */
    @DataProvider(name = "datas")
    public Object[] datas() throws Exception {
        return ReadExcel.readExcel(startSheetIndex, sheetNum).toArray();
    }

    // 不重写 extractResponseVars()，表示该接口不提取响应字段
}
