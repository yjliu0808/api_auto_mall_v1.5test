package com.testcase;

import com.entity.CaseInfo;
import com.excelutils.ReadExcel;
import com.parameters.AddMenuParams;
import com.parameters.AddRoleParams;
import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @Author： Athena
 * @Date： 2025-04-08
 * @Desc： 添加菜单
 **/
public class AddMenuCase extends BaseCase{
    /**
     * 添加菜单接口测试方法，使用统一执行流程
     */
    @Epic("菜单模块")
    @Feature("添加菜单功能")
    @Story("--------------------？？")
    @Owner("Athena")
    @Severity(SeverityLevel.CRITICAL)
    @Description("验证添加菜单的接口是否正常")
    @Test(description = "添加菜单接口测试", dataProvider = "datas")
    public void testRegister(CaseInfo caseInfo) {
        executeTestCase(caseInfo, AddMenuParams::paramsSetValue);
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



