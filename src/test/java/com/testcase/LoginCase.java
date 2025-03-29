package com.testcase;

import com.databaseutils.SqlUtils;
import com.entity.CaseInfo;
import com.excelutils.ReadExcel;
import com.globaldata.GlobalSaveData;
import com.httprequest.GetHeaders;
import com.parameters.LoginParams;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Epic("用户模块")
@Feature("登录功能")
public class LoginCase extends BaseCase {

    /**
     * 登录接口测试方法，使用统一执行流程
     */
    @Test(description = "登录接口测试", dataProvider = "datas")
    public void testLogin(CaseInfo caseInfo) {
        // 传入用例数据 和 登录初始化参数方法
        executeTestCase(caseInfo, LoginParams::paramsSetValue);
    }

    /**
     * 响应中提取 token 并保存到全局变量
     * 会被 BaseCase 中的 executeTestCase 自动调用
     */
    @Override
    protected void extractResponseVars(String response) {
        saveResponseResult(response, "$.data.token=${token}");
    }

    /**
     * 数据提供者：从 Excel 指定 sheet 中读取登录接口用例
     */
    @DataProvider(name = "datas")
    public Object[] datas() throws Exception {
        return ReadExcel.readExcel(startSheetIndex, sheetNum).toArray();
    }
}
